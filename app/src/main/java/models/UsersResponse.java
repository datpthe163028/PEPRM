package models;

import java.util.List;

public class UsersResponse {

    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<User> data;

    // Getters
    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return per_page;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public List<User> getData() {
        return data;
    }

    public static class User {
        private int id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;

        // Getters
        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return first_name;
        }

        public String getLastName() {
            return last_name;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
