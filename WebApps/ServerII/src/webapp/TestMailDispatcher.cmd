@echo on

java -cp WEB-INF/classes;WEB-INF/lib/javamail.jar;WEB-INF/lib/activation.jar;WEB-INF/lib/dimdimClient.jar com.dimdim.conference.application.email.EmailDispatchManager  "jgpandit@yahoo.com,Jayant.Pandit@communiva.com,Prakash.Khot@communiva.com,prakashkhot@yahoo.com" "Test conference" "Welcome to test conference" 2


