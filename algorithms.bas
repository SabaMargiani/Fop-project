' Sum of First N Numbers
LET N = 10  ' Example value for N
LET sum = 0
FOR LET i = 1 TO N
    LET sum = sum + i
NEXT i
PRINT "Sum of first "; N; " numbers is "; sum

' Factorial of N
LET N = 5  ' Example value for N
LET factorial = 1
FOR LET i = 1 TO N
    LET factorial = factorial * i
NEXT i
PRINT "Factorial of "; N; " is "; factorial

' Reverse a Number
LET num = 1234  ' Example value for num
LET reverse = 0
LET initial = 1234
WHILE num > 0
    LET digit = num MOD 10
    LET reverse = reverse * 10 + digit
    LET num = num \ 10
WEND
PRINT initial; "reversed is "; reverse

' Check if a Number is Palindrome
LET num = 121  ' Example value for num
LET original = num
LET reverse = 0
WHILE num > 0
    LET digit = num MOD 10
    LET reverse = reverse * 10 + digit
    LET num = num \ 10
WEND
IF original = reverse THEN
    PRINT original; " is a palindrome"
ELSE
    PRINT original; " is not a palindrome"
END IF

' Find the Largest Digit in a Number
LET num = 3947  ' Example value for num
LET largest = 0
LET initial = 3947
WHILE num > 0
    LET digit = num MOD 10
    IF digit > largest THEN
        LET largest = digit
    END IF
    LET num = num \ 10
WEND
PRINT "Largest digit in "; initial; " is "; largest

' Sum of Digits
LET num = 1234  ' Example value for num
LET sum = 0
LET initial = 1234
WHILE num > 0
    LET digit = num MOD 10
    LET sum = sum + digit
    LET num = num \ 10
WEND
PRINT "Sum of digits in "; initial; " is "; sum

' Multiplication Table
LET num = 7  ' Example value for num
FOR LET i = 1 TO 10
    PRINT num; " x "; i; " = "; num * i
NEXT i

' Nth Fibonacci Number
LET N = 10  ' Example value for N
LET a = 0
LET b = 1
FOR LET i = 3 TO (N + 1)
    LET temp = a + b
    LET a = b
    LET b = temp
NEXT i
IF N = 1 THEN
    PRINT "Fibonacci number #"; N; " is "; a
ELSE IF N = 2 THEN
    PRINT "Fibonacci number #"; N; " is "; b
ELSE
    PRINT "Fibonacci number #"; N; " is "; temp
END IF
