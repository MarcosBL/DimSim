@echo on

java -cp WEB-INF/classes;WEB-INF/lib/javamail.jar;WEB-INF/lib/activation.jar com.dimdim.conference.application.email.MailSender "invitations@communiva.com" "Jayant.Pandit@communiva.com,Prakash.Khot@communiva.com" smtp.atlarge.net invitation.communiva summer2006 1


