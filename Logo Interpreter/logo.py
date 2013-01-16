"""A Logo interpreter."""

import sys
from ucb import interact, main, trace
from buffer import Buffer
from logo_parser import parse_line
import logo_primitives

try:
    import readline
except ImportError:
    pass # Readline is not necessary; it's just a convenience

#############
# Evaluator #
#############

def eval_line(line, env):
    """Evaluate a line (buffer) of Logo.
    
    >>> line = Buffer(parse_line('1 2'))
    >>> eval_line(line, Environment())
    '1'
    >>> line = Buffer(parse_line('print 1 2'))
    >>> eval_line(line, Environment())
    1
    '2'
    """
    "*** YOUR CODE HERE ***"
    while line.current != None:
        result = logo_eval(line, env)
        if result != None:
            return result

def logo_eval(line, env):
    """Evaluate the first expression in a line.

    >>> line = Buffer(parse_line('sum 1 (sum 2 3)'))
    >>> eval_line(line, Environment())
    '6'
    >>> line = Buffer(parse_line('sum 1 (sum 2 3 4)'))
    >>> eval_line(line, Environment())
    Traceback (most recent call last):
        ...
    LogoError: Expected ")" at [ sum, 1, (, sum, 2, 3 >> 4, ) ]
    """
    return eval_noninfix(line, env)

def eval_noninfix(line, env):
    if line.current == None:
        error('Ran out of input at {0}'.format(line))
    elif line.current == ')':
        error('Unexpected ")" at {0}'.format(line))

    token = line.pop()
##    if line.current in INFIX_SYMBOLS.keys():
##        infix = line.pop()
##        arg0 = eval_noninfix(token, env)
##        while infix in INFIX_SYMBOLS.keys():
##            proc = INFIX_SYMBOLS[infix]
##            arg0 = logo_apply(proc, [token, arg0, env])
##        return arg0
    if isprimitive(token):
        return token
    elif isvariable(token):
        return env.lookup_variable(variable_name(token))
    elif isdefinition(token):
        return eval_definition(line, env)
    elif isquoted(token):
        return text_of_quotation(token)
    elif token == '(':
        "*** YOUR CODE HERE ***"
        result = logo_eval(line, env)
        if line.current == ')':
            line.pop()
            return result
        else:
            error('Expected ")" at ' + str(line))
    else:
        procedure = env.procedures.get(token, None)
        if not procedure:
            error('I do not know how to {0}.'.format(token))
        return apply_procedure(procedure, line, env)

def apply_procedure(proc, line, env):
    """Evaluate the procedure named by token on the args in line."""
    args = collect_args(proc.arg_count, line, env)
    if len(args) < proc.arg_count:
        error('Not enough args to {0}'.format(proc.name))
    if proc.needs_env == True:
        args.append(env)
    return logo_apply(proc, args)

def collect_args(n, line, env):
    """Evaluate n arguments from the line via recursive calls to logo_eval.
    
    >>> line = Buffer(parse_line('2 sum 3 4'))
    >>> env = Environment()
    >>> collect_args(2, line, env)
    ['2', '7']
    >>> collect_args(1, line, env)
    Traceback (most recent call last):
        ...
    LogoError: Found only 0 of 1 args at [ 2, sum, 3, 4 >>  ]
    """
    "*** YOUR CODE HERE ***"
    args = []
    args_found = 0
    total_args = n
    while n >= 1:
        if line.current != None:
            args.append(logo_eval(line, env))
            args_found += 1
        else:
            error("Found only " + str(args_found) + " of " + str(total_args) + " args at " + str(line))
        n -= 1
    return args

def logo_apply(proc, args):
    """Apply a Logo procedure to a list of arguments.
    
    >>> body = [['show', ':x'], ['output', 'sum', '1', ':x', ')'], [')']]
    >>> proc = Procedure('f', 1, body, needs_env=True, formal_params=['x'])
    >>> args = ['4', Environment()]
    >>> logo_apply(proc, args)
    4
    '5'
    """
    if proc.isprimitive:
        try:
            return proc.body(*args)
        except Exception as e:
            error(e) # Convert any error into a LogoError
    else:
        "*** YOUR CODE HERE ***"
        params = proc.formal_params
        env = args[-1]
        bound_args = args[:-1]
        frame = {}
        for i in range(len(params)):
            frame[params[i]] = bound_args[i]
        env.push_frame(frame)
        
        for line in proc.body:
            result = eval_line(Buffer(line), env)
            if result != None:
                if result[0] == 'OUTPUT':
                    env.pop_frame()
                    return result[1]
                else:
                    error("You do not say what to do with " + str(result))
        env.pop_frame()

def isoutput(result):
    """Return whether result is a two-element tuple starting with 'OUTPUT'."""
    length_two = type(result) == tuple and len(result) == 2
    return length_two and result[0] == 'OUTPUT'

def isprimitive(token):
    """Numbers, True, and False are primitive, self-evaluating tokens."""
    if token in ('True', 'False'):
        return True
    try:
        float(token)
        return True
    except (TypeError, ValueError):
        return False

def isvariable(exp):
    """Variables start with ":" """
    return type(exp) == str and exp.startswith(':')

def variable_name(exp):
    """Variable names follow the ":" """
    if type(exp) != str or len(exp) <= 1 or exp[0] != ':':
        raise ValueError('Illegal variable expression {0}'.format(exp))
    return exp[1:]

def isdefinition(exp):
    """Definitions start with "to" """
    return exp == "to"

def isquoted(exp):
    """Lists are automatically quoted, and symbols can be explicitly quoted.
    
    >>> isquoted('"hello')
    True
    >>> isquoted('hello')
    False
    >>> isquoted([1, 2])
    True
    """
    "*** YOUR CODE HERE ***"
    return type(exp) == list or exp[0] == '"'

def text_of_quotation(exp):
    """Retrieving the text of a quotation requires stripping the quote.
    
    >>> text_of_quotation('"hello')
    'hello'
    >>> text_of_quotation([1, 2])
    [1, 2]
    """
    "*** YOUR CODE HERE ***"
    if type(exp) == str:
        return exp[1:]
    if type(exp)== list:
        return exp


########################
# Primitive Procedures #
########################

def logo_type(x, top_level=True):
    """Apply the "type" primitive, which prints out a value x.
    
    >>> logo_type(['1', '2', ['3', ['4'], '5']])
    1 2 [3 [4] 5]
    >>> line = Buffer(parse_line('type [a [b c] d]'))
    >>> eval_line(line, Environment())
    a [b c] d
    """
    if type(x) != list:
        print(x, end='') # The end argument prevents starting a new line
    else:
        "*** YOUR CODE HERE ***"
        if x == []:
            print(x, end='')
        elif top_level:
            for i in x[:-1]:
                if type(i) != list:
                    print(i, end=' ')
                else:
                    logo_type(i, False)
                    print(end=' ')
            if type(x[-1]) == list:
                logo_type(x[-1], False)
            else:
                print(x[-1], end='')
        else:
            print('[',end='')
            for i in x[:-1]:
                if type(i) != list:
                    print(i, end=' ')
                else:
                    logo_type(i, False)
                    print(end=' ')
            if type(x[-1]) == list:
                logo_type(x[-1], False)
                print(']', end='')
            else:
                print(x[-1], end='')
                print(']', end='')

def logo_run(exp, env):
    """Apply the "run" primitive."""
    if type(exp) != list:
        exp = [exp]
    return eval_line(Buffer(exp), env)

def logo_if(val, exp, env):
    """Apply the "if" primitive, which takes a boolean and a list.
    
    ***YOUR DOCTEST HERE***
    >>> line = Buffer(parse_line('if True [print 3]'))
    >>> eval_line(line, Environment())
    3
    >>> line = Buffer(parse_line('print if False 4'))
    >>> eval_line(line, Environment())
    None
    >>> line = Buffer(parse_line('if 1 2'))
    >>> eval_line(line, Environment())
    Traceback (most recent call last):
        ...
    LogoError: First argument to "if" is not True or False: 1
    """
    "*** YOUR CODE HERE ***"
    if type(val) == str and val == 'True':
        if type(exp) != list:
            return exp
        return eval_line(Buffer(exp), env)
    elif type(val) == str and val == 'False':
        pass
    else:
        error('First argument to "if" is not True or False: ' + str(val))

def logo_ifelse(val, true_exp, false_exp, env):
    """Apply the "ifelse" primitive, which takes a boolean and two lists.
    
    ***YOUR DOCTEST HERE***
    >>> line = Buffer(parse_line('ifelse True [print "Yes] [print "No]'))
    >>> eval_line(line, Environment())
    Yes
    >>> line = Buffer(parse_line('ifelse False [print "Yes] [print "No]'))
    >>> eval_line(line, Environment())
    No
    >>> line = Buffer(parse_line('print ifelse True 2 3'))
    >>> eval_line(line, Environment())
    2
    >>> line = Buffer(parse_line('ifelse 3 2 1'))
    >>> eval_line(line, Environment())
    Traceback (most recent call last):
        ...
    LogoError: First argument to "ifelse" is not True or False: 3
    """
    "*** YOUR CODE HERE ***"
    if type(val) == str and val == 'True':
        if type(true_exp) != list:
            return true_exp
        return eval_line(Buffer(true_exp), env)
    elif type(val) == str and val == 'False':
        if type(false_exp) != list:
            return false_exp
        return eval_line(Buffer(false_exp), env)
    else:
        error('First argument to "ifelse" is not True or False: ' + str(val))

def logo_make(symbol, val, env):
    """Apply the Logo make primitive, which binds a name to a value.
    
    >>> line = Buffer(parse_line('make "2 3'))
    >>> env = Environment(None)
    >>> eval_line(line, env)
    >>> env.lookup_variable('2')
    '3'
    """
    env.set_variable_value(symbol, val)

# A dict mapping infix symbols to Logo primitive procedure names.
INFIX_SYMBOLS = {'+': 'sum',
                 '-': 'difference',
                 '*': 'product',
                 '/': 'div',
                 '=': 'equalp',
                 '>': 'greaterp',
                 '<': 'lessp',
                 }

# Precedence levels
INFIX_GROUPS = [['<', '>', '='], ['+', '-'], ['*', '/']]

#################################
# Procedures and Initialization #
#################################

class Procedure(object):
    """A Logo procedure, either primitive or user-defined.

    name: The name of the procedure.  For primitive procedures with multiple
          names, only one is stored here.

    arg_count: Number of arguments required by the procedure.

    body: A Logo procedure body is either:
            a Python function, if isprimitive == True
            a list of lines,   if isprimitive == False

    isprimitive: whether the procedure is primitive.

    needs_env: whether the environment should be passed as an add'l parameter.

    formal_params: list of formal parameter names (user-defined procedures).
    """
    def __init__(self, name, arg_count, body, isprimitive=False,
                 needs_env=False, formal_params=None):
        self.name = name
        self.arg_count = arg_count
        self.body = body
        self.isprimitive = isprimitive
        self.needs_env = needs_env
        if not formal_params:
            formal_params = [str(i) for i in range(arg_count)]
        self.formal_params = formal_params

    def __str__(self):
        params = ' '.join([':'+p for p in self.formal_params])
        return 'to {0} {1}'.format(self.name, params)

def load_primitives():
    """Load primitive Logo procedures."""
    primitives = dict()

    def make_primitive(names, arg_count, fn, **kwds):
        """Create primitive procedures for names."""
        if type(names) == str:
            names = [names]
        kwds['isprimitive'] = True
        procedure = Procedure(names[0], arg_count, fn, **kwds)
        for name in names:
            primitives[name] = procedure

    logo_primitives.load(make_primitive)
    make_primitive('type', 1, logo_type)
    make_primitive('make', 2, logo_make, needs_env=True)
    make_primitive('if', 2, logo_if, needs_env=True)
    make_primitive('ifelse', 3, logo_ifelse, needs_env=True)

    make_primitive('output', 1, lambda x: ('OUTPUT', x))
    make_primitive('stop', 0, lambda: ('OUTPUT', None))
    make_primitive('run', 1, logo_run, needs_env=True)
    return primitives


############################################
# Environments and User-Defined Procedures #
############################################

class Environment(object):
    """An environment holds procedure (global) and name bindings in frames."""
    def __init__(self, get_continuation_line=None):
        self.get_continuation_line = get_continuation_line
        self.procedures = load_primitives()
        self._frames = [dict()] # The first frame is the global one

    def push_frame(self, frame):
        """Add a new frame, which contains new bindings."""
        self._frames.append(frame)

    def pop_frame(self):
        """Discard the last frame."""
        self._frames.pop()

    def lookup_variable(self, symbol):
        """Look up a variable in the environment, or raise an error.
        
        >>> env = Environment()
        >>> env.set_variable_value('x', 1)
        >>> env.push_frame({'x': 2, 'y': 3})
        >>> env.push_frame({'y': 4})
        >>> env.lookup_variable('y')
        4
        >>> env.lookup_variable('x')
        2
        >>> env.lookup_variable('z')
        Traceback (most recent call last):
            ...
        LogoError: z has no value
        """
        "*** YOUR CODE HERE ***"
        for i in range(len(self._frames)):
            curr_frame = self._frames[len(self._frames) - 1 - i]
            if symbol in curr_frame.keys():
                return curr_frame[symbol]
        error(symbol + ' has no value')

##        length = len(self._frames)-1
##        while length >= 0:
##            frame = self._frames[length]
##            if symbol in frame.keys():
##                return frame[symbol]
##            length -= 1
##        error(symbol + ' has no value')

##        return self._frames[0][symbol] # Does not scope

    def set_variable_value(self, symbol, val):
        """Set the value of a variable in the innermost frame where it's defined,
        or create it in the global frame.
        
        >>> env = Environment()
        >>> env.set_variable_value('x', 1)
        >>> env.push_frame({'x': 2, 'y': 3})
        >>> env.set_variable_value('x', 4)
        >>> env.lookup_variable('x')
        4
        >>> env.set_variable_value('z', 5)
        >>> env.lookup_variable('z')
        5
        >>> env.pop_frame()
        >>> env.lookup_variable('x')
        1
        >>> env.lookup_variable('z')
        5
        """
        "*** YOUR CODE HERE ***"
        var_set = False # variable not set
        for i in range(len(self._frames)):
            if var_set == False:
                curr_frame = self._frames[len(self._frames) - 1 - i] # sets current frame to last frame not yet iterated on
                if symbol in curr_frame.keys():
                    curr_frame[symbol] = val # set variable
                    var_set = True
        if not var_set:
            self._frames[0][symbol] = val # if symbol not found,sets variable in global frame

##        set_val = False
##        length = len(self._frames)-1
##        while length >= 0:
##            if set_val == False:
##                frame = self._frames[length]
##                if symbol in frame.keys():
##                    set_val = True
##            length -= 1
##        if set_val == False:
##            self._frames[0][symbol] = val

    def __str__(self):
        return ';'.join([str(f) for f in self._frames])

def eval_definition(line, env):
    """Evaluate a definition and create a corresponding procedure.

    line: The definition line, following "to", of the multi-line definition.

    Hint: create a user-defined Procedure object using
        Procedure(name, len(formal_params), body, False, True, formal_params)
        - name is a string defining the procedure name
        - body is a list of lists representing Logo sentences (one per line)
        - False indicates that it is not a primitive procedure
        - True indicates that evaluation requires the environment
        - formal_params is a list of strings naming each formal parameter

    If you were to evaluate the following Logo definition,
    
    ? to double :n
    > output sum :n :n
    > end

    the Procedure object you would create should be equivalent to:

    Procedure('double', 1, [['output', 'sum', ':n', ':n']], False, True, ['n'])

    Note: The doctest for this function was removed because of cross-platform
    compatibility issues with the Python doctest module. (11/17 @ 3:15 PM)
    """
    procedure_name = line.pop()
    next_line = lambda: parse_line(env.get_continuation_line())
    "*** YOUR CODE HERE ***"
    params = []
    param_count = 0
    while line.current != None:
        param = line.pop()
        params.append(param[1:])
        param_count += 1
    body = []
    new_line = next_line()
    while new_line != ['end']:
        body.append(new_line)
        new_line = next_line()
    new_proc = Procedure(procedure_name, param_count, body, False, True, params)
    env.procedures[procedure_name] = new_proc


###############
# Interpreter #
###############

class LogoError(Exception):
    """An error raised by the Logo interpreter."""

def error(message):
    """Raise a Logo error as a Python exception."""
    raise LogoError(message)

def interpret_line(line, env):
    """Interpret a single line in the read-eval loop."""
    result = eval_line(Buffer(parse_line(line)), env)
    if result is not None:
        error('You do not say what to do with {0}.'.format(result))
    
def read_eval_loop(env, get_next_line):
    """Run a read-eval loop for Logo.

    get_next_line: a zero-argument fn that returns a line of Logo code (str).
    """
    while True:
        try:
            line = get_next_line()
            if line.lower() in {'quit', 'exit', 'bye'}:
                raise EOFError
            interpret_line(line, env)
        except (LogoError, SyntaxError) as err:
            print(err)
        except (KeyboardInterrupt, EOFError):
            print('Goodbye!')
            return

def strip_comment(line):
    """Return the prefix of line preceding the first semicolon."""
    return line.split(';', 1)[0]

def prompt_for_line(prompt='?'):
    """Read a line interactively from the user (via standard input)."""
    return strip_comment(input(prompt + ' '))

def generate_lines(src, prompt='?'):
    """Return a function that returns lines from src, a list of strings."""
    def pop_line():
        if not src:
            raise EOFError
        line = src.pop(0)
        print(prompt, line, end='')
        return strip_comment(line)
    return pop_line

@main
def run_interpreter(src_file=None):
    """Run a read-eval loop that reads from either a prompt or a file."""
    get_next_line = prompt_for_line
    get_continuation_line = lambda: prompt_for_line('>')
    if src_file != None:
        src = open(src_file).readlines()
        get_next_line = generate_lines(src)
        get_continuation_line = generate_lines(src, prompt='>')
    env = Environment(get_continuation_line)
    read_eval_loop(env, get_next_line)
