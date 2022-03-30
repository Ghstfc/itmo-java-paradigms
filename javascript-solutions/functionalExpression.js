"use strict";
let variable = f => (...args) => {
    switch (f) {
        case 'x':
            return args[0];
        case 'y':
            return args[1];
        case 'z':
            return args[2];
    }
}
let cnst = f => () => f;
let add = (f1, f2) => (...args) => f1(...args) + f2(...args);
let subtract = (f1, f2) => (...args) => f1(...args) - f2(...args);
let divide = (f1, f2) => (...args) => f1(...args) / f2(...args);
let multiply = (f1, f2) => (...args) => f1(...args) * f2(...args);
let negate = f => (...args) => -f(...args);
let pi = cnst(Math.PI);
let e = cnst(Math.E);