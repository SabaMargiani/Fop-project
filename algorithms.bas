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

' GCD of Two Numbers
LET a = 48  ' Example value for a
LET b = 18  ' Example value for b
WHILE b <> 0
    LET remainder = a MOD b
    LET a = b
    LET b = remainder
WEND
PRINT "GCD is "; a

' Reverse a Number
LET num = 1234  ' Example value for num
LET reverse = 0
WHILE num > 0
    LET digit = num MOD 10
    LET reverse = reverse * 10 + digit
    LET num = num \ 10
WEND
PRINT "Reversed number is "; reverse

' Check if a Number is Prime
LET N = 13  ' Example value for N
LET isPrime = 1
FOR LET i = 2 TO SQR(N)
    IF N MOD i = 0 THEN
        LET isPrime = 0
        EXIT FOR
    END IF
NEXT i
IF N > 1 AND isPrime THEN
    PRINT N; " is a prime number"
ELSE
    PRINT N; " is not a prime number"
END IF

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
WHILE num > 0
    LET digit = num MOD 10
    IF digit > largest THEN
        LET largest = digit
    END IF
    LET num = num \ 10
WEND
PRINT "Largest digit is "; largest

' Sum of Digits
LET num = 1234  ' Example value for num
LET sum = 0
WHILE num > 0
    LET digit = num MOD 10
    LET sum = sum + digit
    LET num = num \ 10
WEND
PRINT "Sum of digits is "; sum

' Multiplication Table
LET num = 7  ' Example value for num
FOR LET i = 1 TO 10
    PRINT num; " x "; i; " = "; num * i
NEXT i

' Nth Fibonacci Number
LET N = 10  ' Example value for N
LET a = 0
LET b = 1
FOR LET i = 3 TO N
    LET temp = a + b
    LET a = b
    LET b = temp
NEXT i
IF N = 1 THEN
    PRINT "Fibonacci number is "; a
ELSEIF N = 2 THEN
    PRINT "Fibonacci number is "; b
ELSE
    PRINT "Fibonacci number is "; temp
END IF
