"use strict";
let ExprPrototype = {

    evaluate: function (x, y, z) {

    },
    toString: function () {

    }
}

function Variable(value) {
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
            return this.value;
        },
        parse: function () {
            return this.value;
        }
    }
}

function Const(value) {
    return {
        value: value,
        evaluate: function () {
            return Number(this.value);
        },
        toString: function () {
            return String(this.value);
        }
    }
}

function Negate(value) {
    return {
        value: value,
        evaluate: function (x, y, z) {
            return -(this.value.evaluate(x, y, z));
        },
        toString: function () {
            return this.value + " negate";
        },
    }
}

function Sinh(value) {
    return {
        value: value,
        evaluate: function (x, y, z) {
            return Math.sinh(this.value.evaluate(x, y, z));
        },
        toString: function () {
            return String(value) + " sinh";
        }
    }
}


function Cosh(value) {
    return {
        value: value,
        evaluate: function (x, y, z) {
            return Math.cosh(this.value.evaluate(x, y, z));
        },
        toString: function () {
            return String(value) + " cosh";
        }
    }
}

function Add(v1, v2) {
    return {
        v1: v1,
        v2: v2,
        evaluate: function (x, y, z) {
            return v1.evaluate(x, y, z) + v2.evaluate(x, y, z);
        },
        toString: function () {
            return v1.toString() + " " + v2.toString() + " +";
        }
    }
}

function Subtract(v1, v2) {
    return {
        v1: v1,
        v2: v2,
        evaluate: function (x, y, z) {
            return v1.evaluate(x, y, z) - v2.evaluate(x, y, z);
        },
        toString: function () {
            return v1.toString() + " " + v2.toString() + " -";
        }
    }
}

function Multiply(v1, v2) {
    return {
        v1: v1,
        v2: v2,
        evaluate: function (x, y, z) {
            return v1.evaluate(x, y, z) * v2.evaluate(x, y, z);
        },
        toString: function () {
            return v1.toString() + " " + v2.toString() + " *";
        }
    }
}

function Divide(v1, v2) {
    return {
        v1: v1,
        v2: v2,
        evaluate: function (x, y, z) {
            return v1.evaluate(x, y, z) / v2.evaluate(x, y, z);
        },
        toString: function () {
            return v1.toString() + " " + v2.toString() + " /";
        }
    }
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
