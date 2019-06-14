function getTokens(input) {

    function check_token(token) {
        let ok = true;
        let type = '';
        switch (token) {
            case '\\times':
                token = '*';
                type = 'operation';
                break;
            case '\\div':
                token = '/';
                type = 'operation';
                break;
            case '\\over':
                type = 'function';
                break;
            case '\\sqrt':
                type = 'function';
                break;
            case '\\frac':
                type = 'function';
                break;
            case '\\leqslant':
                type = 'function';
                break;
            case '\\geqslant':
                type = 'function';
                break;
            default:
                ok = false;
                break;
        }
        return [ok, [type, token]];
    }

    let tokens = [];

    for (let i = 0; i < input.length; i++) {
        let current_char = input.charAt(i);

        if (current_char === '\\') {
            ++i;
            let regex = /[a-zA-Z]/;
            let str = '\\';
            current_char = input.charAt(i);
            while (i < input.length && regex.test(current_char)) {
                str += current_char;
                ++i;
                current_char = input.charAt(i);
            }

            let result = check_token(str);
            if (result[0] === false) {
                return [false, str];
            }

            tokens.push(result[1]);
            --i;
        }
        else if (current_char === '>')
        {
            ++i;
            current_char = input.charAt(i);
            if (current_char === '=') {
                tokens.push(['operation', '>=']);
            } else {
                tokens.push(['operation', '>']);
                --i;
            }
        }
        else if (current_char === '<')
        {
            ++i;
            current_char = input.charAt(i);
            if (current_char === '=') {
                tokens.push(['operation', '<=']);
            } else {
                tokens.push(['operation', '<']);
                --i;
            }
        }
        else if (/[({[\]})]/.test(current_char))//check if '{' '[' '(' '}' ']' ')'
        {
            tokens.push(['bracket', current_char]);
        }
        else if (/[a-zA-Z]/.test(current_char)) {
            let str = '';
            let regex = /[a-zA-Z]/;
            while (i < input.length && regex.test(current_char)) {
                str += current_char;
                ++i;
                current_char = input.charAt(i);
            }
            tokens.push(['variable', str]);
            --i;
        }
        else if (/[0-9.]/.test(current_char)) {
            let str = '';
            let regex = /[0-9.]/;
            while (i < input.length && regex.test(current_char)) {
                str += current_char;
                ++i;
                current_char = input.charAt(i);
            }
            tokens.push(['number', str]);
            --i;
        }
        else if (/[+\-*/^]/.test(current_char)) {
            tokens.push(['operation', current_char]);
        }
    }
    return [true, tokens];
}

function checkSyntax(tokens) {
    let problem = false;
    let empty_parenthesis = false;
    let problem_description = '';
    let last_token = null;
    let brace = 0;
    let parenthesis = 0;
    let bracket = 0;
    for (let i = 0; i < tokens.length; i++) {

        if (tokens[i][0] === 'bracket') {
            if (i !== 0) {
                if (last_token[0] === 'bracket') {
                    switch (last_token[1]) {
                        case '(':
                            if (tokens[i][1] === ')') {
                                empty_parenthesis = true;
                            }
                            break;
                        case '[':
                            if (tokens[i][1] === ']') {
                                empty_parenthesis = true;
                            }
                            break;
                        case '{':
                            if (tokens[i][1] === '}') {
                                empty_parenthesis = true;
                            }
                    }
                }
            }
            switch (tokens[i][1]) {
                case '(':
                    ++parenthesis;
                    break;
                case '[':
                    ++bracket;
                    break;
                case '{':
                    ++brace;
                    break;
                case ')':
                    --parenthesis;
                    break;
                case ']':
                    --bracket;
                    break;
                case '}':
                    --brace;
                    break;
            }
        }

        last_token = tokens[i];
    }
    if (empty_parenthesis) {
        problem = true;
        problem_description += 'Empty parentheses, braces or brackets' + '<br>';
    }
    if (parenthesis !== 0) {
        problem = true;
        problem_description += 'Uneven number of parentheses' + '<br>';
    }
    if (brace !== 0) {
        problem = true;
        problem_description += 'Uneven number of braces' + '<br>';
    }
    if (bracket !== 0) {
        problem = true;
        problem_description += 'Uneven number of brackets';
    }

    if (problem) {
        return [false, problem_description];
    }
    else {
        return [true, ''];
    }
}

function mapVariables(tokens, variables_list, dataPropertiesArray) {
    let problem = false;
    let list_of_problems = [];
    for (let i = 0; i < tokens.length; i++) {
        let current_token = tokens[i];
        if (current_token[0] === 'variable') {
            if (variables_list.has(current_token[1])) {
                tokens[i][1] = variables_list.get(current_token[1]);
            }
            else if (!dataPropertiesArray.includes(current_token[1])) {
                problem = true;
                if (!list_of_problems.includes(current_token[1])) {
                    list_of_problems.push(current_token[1]);
                }
            }
        }
    }
    if (problem) {
        return [false, list_of_problems];
    }
    return [true, tokens];
}

function normalize(tokens) {
    for (let i = 0; i < tokens.length; i++) {
        if (tokens[i][1] === '\\frac') {
            ++i;
            let bracket_num = 0;
            while (i < tokens.length) {
                if (tokens[i][1] === '{') {
                    bracket_num++;
                }
                else if (tokens[i][1] === '}') {
                    bracket_num--;
                }
                if (bracket_num === 0) {
                    break;
                }
                ++i;
            }
            //tokens.insert(i+1, ['operation','/']);
            tokens.splice(i + 1, 0, ['operation', '/']);
            tokens = tokens.filter(function (value) {
                return value[1] !== '\\frac';
            })
        }
        else if (tokens[i][0] === 'variable' || tokens[i][0] === 'number') {
            if ((i + 1) < tokens.length) {
                if (tokens[i + 1][0] === 'variable' || tokens[i + 1][0] === 'number') {
                    tokens.splice(i + 1, 0, ['operation', '*']);
                }
            }
        }
    }
    return tokens;
}

function reversePolishNotation(tokens) {
    let rpn = [];
    let stack = [];

    for (let i = 0; i < tokens.length; i++) {
        let current_token = tokens[i];
        switch (current_token[0]) {
            case 'number':
                rpn.push(current_token);
                break;
            case 'variable':
                rpn.push(current_token);
                break;
            case 'function':
                switch (current_token[1]) {
                    case '\\over':
                        while (stack.length > 0 && stack[stack.length - 1][1] !== '{') {
                            rpn.push(stack.pop());
                        }
                        stack.push(['operation', '/']);
                        break;
                    case '\\sqrt':
                        if (tokens[i + 1][1] === "[" && tokens[i + 2][1] !== "]") {
                            current_token[1] = '\\root'
                        }
                        stack.push(current_token);
                        break;
                    case '\\leqslant':
                        while (stack.length > 0 && stack[stack.length - 1][1] !== '{') {
                            rpn.push(stack.pop());
                        }
                        stack.push(['operation', '<=']);
                        break;
                    case '\\geqslant':
                        while (stack.length > 0 && stack[stack.length - 1][1] !== '{') {
                            rpn.push(stack.pop());
                        }
                        stack.push(['operation', '>=']);
                        break;
                    default:
                        rpn.push(current_token);
                }
                break;
            case 'operation':
                let current_order;
                if (/[+\-]/.test(current_token[1])) {
                    current_order = 0;
                }
                else if (/[*\/]/.test(current_token[1])) {
                    current_order = 1;
                }
                else if (/\^/.test(current_token[1])) {
                    current_order = 2;
                }
                while (stack.length > 0 && stack[stack.length - 1][0] !== 'operation') {
                    let stack_oder = -1;
                    if (/[+\-]/.test(stack[stack.length - 1][0])) {
                        stack_oder = 0;
                    }
                    else if (/[*\/]/.test(stack[stack.length - 1][0])) {
                        stack_oder = 1;
                    }
                    else if (/\^/.test(stack[stack.length - 1][0])) {
                        stack_oder = 2;
                    }

                    if (current_token[1] === '^') {
                        if (stack_oder > current_order) {
                            rpn.push(stack.pop());
                        }
                        else {
                            break;
                        }
                    }
                    else {
                        if (stack_oder >= current_order) {
                            rpn.push(stack.pop());
                        }
                        else {
                            break;
                        }
                    }
                }
                stack.push(current_token);
                break;
            case 'bracket':
                if (/[([{]/.test(current_token[1])) {
                    stack.push(current_token);
                }
                else if (current_token[1] === '}') {
                    while (stack.length > 0) {
                        if (stack[stack.length - 1][1] !== '{') {
                            rpn.push(stack.pop());
                        }
                        else {
                            stack.pop();
                            break;
                        }
                    }
                }
                else if (current_token[1] === ']') {
                    while (stack.length > 0) {
                        if (stack[stack.length - 1][1] !== '[') {
                            rpn.push(stack.pop());
                        }
                        else {
                            stack.pop();
                            break;
                        }
                    }
                }
                else if (current_token[1] === ')') {
                    while (stack.length > 0) {
                        if (stack[stack.length - 1][1] !== '(') {
                            rpn.push(stack.pop());
                        }
                        else {
                            stack.pop();
                            break;
                        }
                    }
                }
                // if(stack.length > 0 && stack[stack.length - 1][0] === 'function')
                // {
                //     rpn.push(stack.pop());
                // }
                break;
        }
    }
    while (stack.length > 0) {
        rpn.push(stack.pop());
    }
    return rpn;
}

function convertoToJson(rpn) {
    let stack = [];
    let json, a, b;

    for (let i = 0; i < rpn.length; i++) {
        let current_element = rpn[i];
        switch (current_element[0]) {
            case 'number':
                stack.push({tag: "CONSTANT", constant: current_element[1]});
                break;
            case 'variable':
                stack.push({tag: "VARIABLE", variable: current_element[1]});
                break;
            case 'operation':
                json = {};
                json['tag'] = "OPERATION";
                switch (current_element[1]) {
                    case '^':
                        json['operation'] = "EXPONENTIATION";
                        break;
                    case '/':
                        json['operation'] = "DIVISION";
                        break;
                    case '*':
                        json['operation'] = "MULTIPLICATION";
                        break;
                    case '+':
                        json['operation'] = "ADDITION";
                        break;
                    case '-':
                        json['operation'] = "SUBTRACTION";
                        break;
                    case '<=':
                        json['operation'] = "LESS_OR_EQUAL_THAN";
                        break;
                    case '<':
                        json['operation'] = "LESS_THAN";
                        break;
                    case '>=':
                        json['operation'] = "GREATER_OR_EQUAL_THAN";
                        break;
                    case '>':
                        json['operation'] = "GREATER_THAN";
                        break;
                }
                a = stack.pop();
                b = stack.pop();
                json['firstElement'] = b;
                json['secondElement'] = a;
                stack.push(json);
                break;
            case 'function':
                json = {};
                json['tag'] = "OPERATION";
                switch (current_element[1]) {
                    case '\\root':
                        json['operation'] = "ROOT";
                        break;
                    case '\\sqrt':
                        json['operation'] = "ROOT";
                        break;
                    case '\\frac':
                        json['operation'] = "DIVISION";
                        break;
                    case '\\leqslant':
                        json['operation'] = "LESS_OR_EQUAL_THAN";
                        break;
                    case '\\geqslant':
                        json['operation'] = "GREATER_OR_EQUAL_THAN";
                        break;
                }
                a = stack.pop();
                if (current_element[1] === '\\sqrt') {
                    b = {tag: "CONSTANT", constant: "2"}
                }
                else {
                    b = stack.pop();
                }
                json['firstElement'] = b;
                json['secondElement'] = a;
                stack.push(json);
                break;

        }
    }
    return stack.pop();
}