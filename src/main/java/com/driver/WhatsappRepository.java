package com.driver;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class WhatsappRepository {

    HashMap<String, User> userDb = new HashMap<>();
    HashMap<String, List<User>> GroupDb = new HashMap<>();

    HashMap<Integer, Message> messageDb = new HashMap<>();

    public String createUser(String name, String mobile) throws Exception {
        if(userDb.containsKey(mobile)){
            throw new Exception("User already exists");
        }
        else{
            User user = new User(name, mobile);
            userDb.put(mobile, user);
            return "SUCCESS";
        }

    }

    public Group createGroup(List<User> users){
        int count = 0;
        Group group = new Group();
        if(users.size() == 2){
            // Personal chat
            GroupDb.put(users.get(1).getName(), users);
            group.setName(users.get(1).getName());
            group.setNumberOfParticipants(users.size());
            return group;
        }
        else{
            count++;
            // Group chat
            GroupDb.put("Group"+count, users);
            group.setName("Group"+count);
            group.setNumberOfParticipants(users.size());
            return group;
        }
    }

    public int createMessage(String content){
        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(new Date());
        message.setId(1);
        messageDb.put(1, message);

        return message.getId();
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        List<User> userList = GroupDb.get(group.getName());

        if(userList.contains(sender) == false){
            throw new Exception("You are not allowed to send message");
        }

        if(GroupDb.containsKey(group.getName()) == false){
            throw new Exception("Group does not exist");
        }


     return 1;
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(GroupDb.containsKey(group.getName()) == false) throw new Exception("Group does not exist");
        List<User> userList = GroupDb.get(group.getName());
        User currAdmin = userList.get(0);
        if(currAdmin != approver) throw new Exception("Approver does not have rights");

        if(userList.contains(user) == false) throw new Exception("User is not a participant");

//        if(userList.get(0) == approver){
//            group.setName(currAdmin.getName());
//        }
        userList.set(0,user);

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        boolean flag = false;
        for(String userKey : userDb.keySet()){
            if(userKey == user.getName()){
                flag = true;
            }
        }
        if(flag == false) throw new Exception("User not found");


        for(String groupName : GroupDb.keySet()){
            if(GroupDb.get(groupName).contains(user) == true){
                flag = true;
                if(GroupDb.get(groupName).get(0).getName() == user.getName()){
                    throw new Exception("Cannot remove admin");
                }
                else{
                    GroupDb.get(groupName).remove(user);
                    user.setName(null);
                    user.setMobile(null);
                }
                break;
            }
        }
        if(flag == false) throw new Exception("User not found");

        return GroupDb.get(user.getName()).size()+1+1;
    }

    public String findMessage(Date start, Date end, int K){
        return null;
    }

}
