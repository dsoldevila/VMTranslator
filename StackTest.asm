//Push
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
//eq
@SP
AM=M-1
D=M
@SP
AM=M-1
D=D-M
M=0
@LABEL_0
D;JNE
@SP
A=M
M=-1
(LABEL_0)
@SP
M=M+1
//Push
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
//eq
@SP
AM=M-1
D=M
@SP
AM=M-1
D=D-M
M=0
@LABEL_1
D;JNE
@SP
A=M
M=-1
(LABEL_1)
@SP
M=M+1
//Push
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
//eq
@SP
AM=M-1
D=M
@SP
AM=M-1
D=D-M
M=0
@LABEL_2
D;JNE
@SP
A=M
M=-1
(LABEL_2)
@SP
M=M+1
//Push
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
//lt
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
M=0
@LABEL_3
D;JGE
@SP
A=M
M=-1
(LABEL_3)
@SP
M=M+1
//Push
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
//lt
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
M=0
@LABEL_4
D;JGE
@SP
A=M
M=-1
(LABEL_4)
@SP
M=M+1
//Push
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
//lt
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
M=0
@LABEL_5
D;JGE
@SP
A=M
M=-1
(LABEL_5)
@SP
M=M+1
//Push
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
//gt
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
M=0
@LABEL_6
D;JLE
@SP
A=M
M=-1
(LABEL_6)
@SP
M=M+1
//Push
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
//gt
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
M=0
@LABEL_7
D;JLE
@SP
A=M
M=-1
(LABEL_7)
@SP
M=M+1
//Push
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
//gt
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
M=0
@LABEL_8
D;JLE
@SP
A=M
M=-1
(LABEL_8)
@SP
M=M+1
//Push
@57
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@31
D=A
@SP
A=M
M=D
@SP
M=M+1
//Push
@53
D=A
@SP
A=M
M=D
@SP
M=M+1
//add
@SP
AM=M-1
D=M
A=A-1
M=M+D
//Push
@112
D=A
@SP
A=M
M=D
@SP
M=M+1
//sub
@SP
AM=M-1
D=M
A=A-1
M=M-D
//neg
@SP
A=M-1
M=-M
//and
@SP
AM=M-1
D=M
A=A-1
M=M&D
//Push
@82
D=A
@SP
A=M
M=D
@SP
M=M+1
//and
@SP
AM=M-1
D=M
A=A-1
M=M|D
//not
@SP
A=M-1
M=!M
