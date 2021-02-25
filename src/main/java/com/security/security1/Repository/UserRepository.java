package com.security.security1.Repository;

import com.security.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



//@Repository 라는 annotation이 없어도 loC가 된다. 그이유는 JpaRepository를 사용했기 때문
    public interface UserRepository extends JpaRepository<User, Integer> {

        //select * from user wher username = 1?
        public User findByUsername(String username);

        public User findByEmail(String email);
}
