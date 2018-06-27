LLEngine  
========  

This is an recreation of the LifeLine Engine/Parser.  
The Story is provided as an TXT file.  


Syntax  
------  

*Normal Text  
```
This is some Normal Text
And Here is some variable <<$var>>
```


*Special Text  
```
[Some Special Text]
[This does also work with <<$var>>]
```


*IF  
```
<<if $var eq "bar">>
<<elseif $bar geq 1>>
<<elseif $foo = 2 and $bar = "foo">>
<<else>>
<<endif>>
```


*SET Variable  
```
<<set $var = "foo">>
<<set $bar = 1>>
<<set $bar += 2>>
<<set $bar = $bar - 3>>
```

TODO  
* Implement IF as pure Script Evaluation  
  * Possible to use ($var + $var2 >= 10)  
  * Define Logical Operations  
