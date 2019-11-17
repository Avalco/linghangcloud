package com.linghangcloud.android.GSON;

public class UserInfo {
    private String code;
    private data data;

    public class data {
        private String account;
        private String imageurl;
        private String nickname;
        private String classname;
        private String group;

        public data(String account, String imageurl, String nickname, String classname, String group) {
            this.account = account;
            this.imageurl = imageurl;
            this.nickname = nickname;
            this.classname = classname;
            this.group = group;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserInfo.data getData() {
        return data;
    }

    public void setData(UserInfo.data data) {
        this.data = data;
    }
}
