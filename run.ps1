# 
Invoke-expression 'start powershell {mvn -f .\orders\ spring-boot:run}'
Invoke-expression 'start powershell {mvn -f .\orchestrator\ spring-boot:run}'
Invoke-expression 'start powershell {mvn -f .\products\ spring-boot:run}'
Invoke-expression 'start powershell {mvn -f .\payments\ spring-boot:run}'