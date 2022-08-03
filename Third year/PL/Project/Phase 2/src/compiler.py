# PLY-simple to PLY

import ply.lex as lex

tokens = [ 'NAME','EXP','COMMENT','EQUAL','PERCENTAGE', 'DEFLEX', 'DEFYACC', 'RETURN', 'RE', 'PL', 'EXPA', 'EXPB', 'EXPC', 'EXPE', 'EXPD', 'LBR', 'RBR', 'COMMA', 'BD', 'ERROR', 'READPRINT', 'ERRORLINE', 'DOUBLEPERCENTAGE', 'LEXFUNCTIONS', 'DEFGRAM', 'YACCGRAMMAR', 'GRAMRES', 'LCHV', 'RCHV']

states = (
    ('func', 'exclusive'),
    ('grammar', 'exclusive'),
    ('readall', 'exclusive'),
)

def t_func_RETURN(t):
    r'return'
    return t

def t_func_ERROR(t):
    r'error'
    return t

def t_EQUAL(t):
    r'='
    return t

def t_func_COMMA(t):
    r','
    return t

def t_func_PL(t):
    r'\''
    return t

def t_func_LBR(t):
    r'\('
    return t

def t_func_RBR(t):
    r'\)'
    return t

def t_grammar_LCHV(t):
    r'\{'
    return t

def t_grammar_RCHV(t):
    r'\}'
    return t

def t_DEFLEX(t):
    r'%%\sLEX\b'
    return t

def t_func_DEFYACC(t):
    r'%%\sYACC'
    t.lexer.begin('INITIAL')
    return t

def t_YACCGRAMMAR(t):
    r'%%\sYACCGRAMMAR'
    t.lexer.begin('grammar')
    return t

def t_LEXFUNCTIONS(t):
    r'%%\sLEXFUNCTIONS'
    t.lexer.begin('func')
    return t

def t_grammar_DOUBLEPERCENTAGE(t):
    r'%%'
    t.lexer.begin('readall')
    return t

def t_PERCENTAGE(t):
    r'%'
    return t

def t_EXPE(t):
    r'r\'.+\''
    return t

def t_NAME(t):
    r'[a-zA-Z]+'
    return t

def t_INITIAL_func_COMMENT(t):
     r'\#.*'
     return t

def t_func_READPRINT(t):
    r'f?\".*\"(\s\+\st\.value\[0\])?'
    return t

def t_func_ERRORLINE(t):
    r'([a-zA-Z].*)\([1-9]\)'
    return t

def t_func_BD(t):
    r'(([a-zA-Z]+)\(([a-zA-Z]).([a-zA-Z]+)\))|([a-zA-Z].([a-zA-Z]+))'
    return t

def t_func_RE(t):
    r'[^\s]+'
    return t

def t_EXPA(t):
    r'".+"'
    return t

def t_EXPB(t):
    r'\[.+\]'
    return t

def t_EXPD(t):
    r'\{.*\}'
    return t

def t_grammar_DEFGRAM(t):
    r'\w+\s.*?(?=\s{2})'
    return t

def t_grammar_GRAMRES(t):
    r'[^\}]+'
    return t

def t_EXPC(t):
    r'[^\]]*\]'
    return t

def t_INITIAL_readall_EXP(t):
    r'.+'
    return t



t_ignore = " \t\n\r"
t_func_ignore = " \t\n\r"
t_grammar_ignore = " \t\n\r"
t_readall_ignore = "\t\n\r"


def t_error(t):
    print(f"Illegal character, '{t.value[0]}', [{t.lexer.lineno}]")
    t.lexer.skip(1)

def t_func_error(t):
    print(f"Illegal character, '{t.value[0]}', [{t.lexer.lineno}]")
    t.lexer.skip(1)

def t_grammar_error(t):
    print(f"Illegal character, '{t.value[0]}', [{t.lexer.lineno}]")
    t.lexer.skip(1)

def t_readall_error(t):
    print(f"Illegal character, '{t.value[0]}', [{t.lexer.lineno}]")
    t.lexer.skip(1)

#build the lexer
lexer = lex.lex()

import ply.yacc as yacc

def p_program(p):
    "App : StartLex StartYacc"
    parser.output = p[1] + "lexer = lex.lex()" + "\n" + p[2]

def p_startLex(p):
    "StartLex : DEFLEX LexMain"
    p[0] = "import ply.lex as lex" + '\n' + '\n' + p[2]

def p_startYacc(p):
    "StartYacc : DEFYACC Yacc"
    p[0] = "\nimport ply.yacc as yacc" + '\n' + '\n' + p[2]

def p_lexMain(p):
    "LexMain : LexVariables LexFunctions"
    p[0] = p[1] + p[2]

def p_lexVariables_empty(p):
    "LexVariables : "
    p[0] = ""

def p_lexVariables_reserved_option(p):
    "LexVariables : LexVariables Reserved Comment"
    p[0] = p[1] + p[2] + p[3]

def p_lexFunctions_function_option(p):
    "LexFunctions : LEXFUNCTIONS LFunctions"
    p[0] = p[2]

def p_lexFunctions_functions_empty(p):
    "LFunctions : "
    p[0] = ""

def p_lexFunctions_functions(p):
    "LFunctions : LFunctions Function Comment"
    p[0] = p[1] + p[2] + p[3]

def p_lexVariables_reserved(p):
    "Reserved : PERCENTAGE NAME EQUAL EXPA"
    if p[2] == "ignore":
        p[0] = "t_" + p[2] + " " + p[3] + " " + p[4] + '\n' + '\n'
    else:
        p[0] = p[2] + " " + p[3] + " " + p[4] + '\n' + '\n'

def p_lexVariables_reserved_bracket(p):
    "Reserved : PERCENTAGE NAME EQUAL EXPB"
    p[0] = p[2] + " " + p[3] + " " + p[4] + " " + '\n' + '\n'

def p_lexVariables_reserved_individual(p):
    "Reserved : PERCENTAGE NAME EQUAL EXPE" # Individual rules
    p[0] = "t_" + p[2] + " " + p[3] + " " + p[4] + " " + '\n' + '\n'

def p_lexFunctions_function_re(p): # Read until space, read return, read name, read body. 
    "Function : RE RETURN LBR PL BD PL COMMA BD RBR " 
    p[0] = "def t_" + p[5] + "(t):" + '\n' + '\t' + "r'" + p[1] + "'" + '\n' + '\t' + "t.value = " + p[8] + '\n' + '\t' + "return t" + '\n' + '\n'
    
def p_lexFunctions_function_error(p):
    "Function : RE ERROR LBR READPRINT COMMA ERRORLINE RBR " 
    p[0] = "def t_" + p[2] + "(t):" + '\n' + '\t' + "print(" + p[4] + ")" + '\n' + '\t' + p[6] + '\n' + '\n'

def p_comment_empty(p):
    "Comment : "
    p[0] = ""

def p_comment(p):
    "Comment : COMMENT"
    p[0] = p[1] + '\n'

def p_yacc_empty(p):
    "Yacc : "
    p[0] = ""

def p_yacc_grammar_option(p):
    "Yacc : Yacc Defgrammar Comment"
    p[0] = p[1] + p[2] + p[3]

def p_yacc_reserved_option(p):
    "Yacc : Yacc Yreserved Comment"
    p[0] = p[1] + p[2] + p[3]

def p_yacc_var_option(p):
    "Yacc : Yacc Yvar Comment"
    p[0] = p[1] + p[2] + p[3]

def p_yacc_readall_option(p):
    "Yacc : Yacc Yall Comment"
    p[0] = p[1] + p[2] + p[3]

def p_yacc_yall(p):
    "Yall : DOUBLEPERCENTAGE Yexp"
    p[0] = p[2]

def p_yacc_yexp_empty(p):
    "Yexp : "
    p[0] = ""

def p_yacc_yexp(p):
    "Yexp : Yexp EXP"
    p[0] = p[1] + p[2] + '\n'

def p_yacc_defgrammar(p):
    "Defgrammar : YACCGRAMMAR Ygrammar"
    p[0] = p[2]

def p_yacc_ygammar_empty(p):
    "Ygrammar : "
    p[0] = ""

def p_yacc_ygammar(p):
    "Ygrammar : Ygrammar DEFGRAM LCHV Grambody RCHV"
    parser.counter += 1
    p[0] = p[1] + "def p_" + str(parser.counter) + "(t):" + '\n' + '\t"' + p[2] + '"\n' + '\t' + p[4] + '\n'

def p_yacc_grambody_empty(p):
    "Grambody : "
    p[0] = ""

def p_yacc_grambody(p):
    "Grambody : GRAMRES"
    p[0] = p[1] + '\n'

def p_yacc_var(p):
    "Yvar : NAME EQUAL EXPD"
    p[0] = p[1] + " " + p[2] + " " + p[3] + '\n' + '\n'

def p_yacc_reserved(p):
    "Yreserved : PERCENTAGE NAME EQUAL EXPC"
    p[0] = p[2] + " " + p[3] + " " + p[4] + '\n' + '\n'

def p_error(p):
    print('Erro sintatico: ', p)

parser = yacc.yacc()
parser.outputL = ""
parser.outputY = ""
parser.output = ""
parser.counter = 0

with open('Fase2\\files\ply-simple_1.txt', 'r') as file:
    data = file.read()
parser.parse(data)

with open('Fase2\out\output.py', 'w+') as file_out:
    file_out.write(parser.output)
