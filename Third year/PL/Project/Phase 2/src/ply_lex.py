import ply.lex as lex

tokens = ['VAR','NUMBER']

literals = ['+', '-', '/', '*', '=', '(', ')']


t_ignore = " \t\n"

def t_VAR(t):
    r'[a-zA-Z_][a-zA-Z0-9_]*'
    t.value = t.value
    return t

def t_NUMBER(t):
    r'\d+(\.\d+)?'
    t.value = float(t.value)
    return t

def t_error(t):
    print(f"Illegal character '{t.value[0]}', [{t.lexer.lineno}]")
    t.lexer.skip(1)

#build the lexer
lexer = lex.lex()

import ply.yacc as yacc

precedence = [
    ('left','+','-'),
    ('left','*','/'),
    ('right','UMINUS'),
]

# symboltable : dictionary of variables
ts = { }

def p_stat_atrib(p):
    "stat : VAR '=' exp"
    ts[p[1]] = p[3]

def p_stat_print(p):
    "stat : exp"
    print(p[1])

def p_exp_soma(p):
    "exp : exp '+' exp"
    p[0] = p[1] + p[3]

def p_exp_sub(p):
    "exp : exp '-' exp"
    p[0] = p[1] - p[3]

def p_exp_mul(p):
    "exp : exp '*' exp"
    p[0] = p[1] * p[3]

def p_exp_div(p):
    "exp : exp '/' exp"
    p[0] = p[1] / p[3]

def p_exp_negar(p):
    "exp : '-' exp %prec UMINUS"
    p[0] = -p[2]

def p_exp_aexp(p):
    "exp : '(' exp ')'"
    p[0] = p[2]

def p_exp_numero(p):
    "exp : NUMBER"
    p[0] = p[1]

def p_exp_var(p):
    "exp : VAR"
    p[0] = getval(p[1])

def p_error(t):
    print(f"Syntax error at '{t.value}', [{t.lexer.lineno}]")


def getval(n):
    if n not in ts: print(f"Undefined name '{n}'")
    return ts.get(n,0)


parser = yacc.yacc()
parser.parse("3+4*7")

