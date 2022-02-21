package site.nomoreparties.stellarburgers;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)

public class SetUser {

        public String email;
        public String password;
        public String name;
        public String accessToken;



    public String getEmail() {
            return email;
        }

        public SetUser setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword(){
            return password;
        }

        public SetUser setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getName() {
            return name;
        }

        public SetUser setName(String name) {
            this.name = name;
            return this;
        }

    public String getAccessToken() {
        return accessToken;
   }

   SetUser(){

   }

    SetUser(String email, String password, String name){
            this.email = email;
            this.password = password;
            this.name = name;

        }

    SetUser(String email, String password){
        this.email = email;
        this.password = password;
    }



}


