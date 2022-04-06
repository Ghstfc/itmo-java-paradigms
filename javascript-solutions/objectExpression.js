"use strict";

function Variable(value) {

    if (value !== 'x' && value !== 'y' && value !== 'z') {
        throw new CustomError("Wrong variable");
    }
    return {
        value: value,
        evaluate: function (x, y, z) {
            switch (this.value) {
                case "x":
                    return x;
                case "y":
                    return y;
                case "z":
                    return z;
            }
        },
        toString: function () {
            return String(this.value);
        },
        prefix: function () {
            return String(this.value);
        },

    }

}


function BinaryOperation(v1, v2, sign, op) {
    return {
        v1: v1,
        v2: v2,
        sign: sign,
        op: op,
        evaluate: function (x, y, z) {
            return this.op(this.v1.evaluate(x, y, z), this.v2.evaluate(x, y, z));
        },
        toString: function () {
            return this.v1.toString() + " " + this.v2.toString() + " " + this.sign;
        },
        prefix: function () {
            return "(" + this.sign + " " + this.v1.prefix() + " " + this.v2.prefix() + ')';
        }
    }
}


function Add(v1, v2) {
    return BinaryOperation(v1, v2, "+", (a, b) => a + b)
}

function Subtract(v1, v2) {
    return BinaryOperation(v1, v2, "-", (a, b) => a - b)
}

function Divide(v1, v2) {
    return BinaryOperation(v1, v2, "/", (a, b) => a / b)
}

function Multiply(v1, v2) {
    return BinaryOperation(v1, v2, "*", (a, b) => a * b)
}


function UnaryOperation(v, sign) {
    return {
        v: v,
        sign: sign,
        evaluate: function (x, y, z) {
            switch (sign) {
                case '+':
                    return Number(v);
                case '-':
                    return -Number(v.evaluate(x, y, z));
                case ' sinh':
                    return Math.sinh(v.evaluate(x, y, z));
                case ' cosh':
                    return Math.cosh(v.evaluate(x, y, z));
            }
        },
        toString: function () {
            switch (sign) {
                case '+':
                    return String(v);
                case '-':
                    return String(v) + ' negate'
                case ' sinh':
                    return String(v) + sign;
                case ' cosh':
                    return String(v) + sign;
            }
        },
        prefix: function () {
            switch (sign) {
                case '+':
                    return String(v);
                case '-':
                    return '(negate ' + String(v.prefix()) + ')';
                case ' sinh':
                    return sign.trim() + " " + String(v);
                case ' cosh':
                    return sign.trim() + " " + String(v);
            }
        }
    }
}

function Const(v) {
    return UnaryOperation(v, '+');
}

function Negate(v) {
    return UnaryOperation(v, '-')
}

function Sinh(v) {
    return UnaryOperation(v, ' sinh');
}

function Cosh(v) {
    return UnaryOperation(v, ' cosh');
}


function parse(str) {
    let perm = String(str).trim().split(" ");
    let stack = [];
    let tmp;
    for (let i = 0; i < perm.length; i++) {
        switch (perm[i]) {
            case 'x':
            case 'y':
            case 'z':
                stack.push(new Variable(perm[i]));
                break;
            case '*':
                tmp = stack.pop();
                stack.push(new Multiply(stack.pop(), tmp));
                break;
            case '/':
                tmp = stack.pop();
                stack.push(new Divide(stack.pop(), tmp));
                break;
            case '+':
                tmp = stack.pop();
                stack.push(new Add(stack.pop(), tmp));
                break;
            case '-':
                tmp = stack.pop();
                stack.push(new Subtract(stack.pop(), tmp));
                break;
            case'negate':
                stack.push(new Negate(stack.pop()));
                break
            case'sinh' :
                stack.push(new Sinh(stack.pop()));
                break;
            case'cosh':
                stack.push(new Cosh(stack.pop()));
                break;
        }
        if (!isNaN(Number(perm[i])) && perm[i] !== '') {
            stack.push(new Const(perm[i]));
        }
    }
    return stack.pop();
}






















function CustomError(message) {
    Error.call(this, message);
    this.message = message;
}

CustomError.prototype = Object.create(Error.prototype);
CustomError.prototype.name = "CustomError";

function parsePrefix(str) {
    check(str);
    str = str.replaceAll("(", " ");
    str = str.replaceAll(")", " ");
    let perm = String(str).trim().split(" ");
    perm = perm.filter((n) => n !== "");
    let stack = [];
    for (let i = perm.length - 1; i >= 0; i--) {
        if (!isNaN(Number(perm[i]))) {
            stack.push(new Const(perm[i]));
            continue;
        }
        switch (perm[i]) {
            case 'x':
            case 'y':
            case 'z':
                stack.push(new Variable(perm[i]));
                break;
            case '*':
                if (stack.length === 1)
                    throw new CustomError("Bruh");
                stack.push(new Multiply(stack.pop(), stack.pop()));
                break;
            case '/':
                if (stack.length === 1)
                    throw new CustomError("Bruh");
                stack.push(new Divide(stack.pop(), stack.pop()));
                break;
            case '+':
                if (stack.length === 1)
                    throw new CustomError("Bruh");
                stack.push(new Add(stack.pop(), stack.pop()));
                break;
            case '-':
                if (stack.length === 1)
                    throw new CustomError("Bruh");
                stack.push(new Subtract(stack.pop(), stack.pop()));
                break;
            case'negate':
                stack.push(new Negate(stack.pop()));
                break
            case'sinh' :
                stack.push(new Sinh(stack.pop()));
                break;
            case'cosh':
                stack.push(new Cosh(stack.pop()));
                break;
            default:
                throw new CustomError("wrong token");
        }
    }
    if (stack.length !== 1)
        throw new CustomError("wrong expr");
    return stack.pop();
}

function check(s) {
    let count = 0;
    for (let i = 0; i < s.length; i++) {
        if (s[i] === '(')
            count++;
        else if (s[i] === ')')
            count--;
        if (count < 0) {
            throw new CustomError("Wrong bracket sequence");
        }
    }
    if (count > 0)
        throw new CustomError("Wrong bracket sequence");
    return true;
}
