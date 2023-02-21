package com.example.btrack.service;


import com.example.btrack.dto.AddAssistant;
import com.example.btrack.models.Assist;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.AssistRepository;
import com.example.btrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssistService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssistRepository assistRepository;

    public ResponseEntity<Object> getAssistants(String idToken,String name) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Assist> assistantsrecords = assistRepository.findByUser_UsernameAndAssistant_UsernameLike(actualUser.getUsername(),name);
                List<Userdetails> userdetails = userRepository.findByUsernameIgnoreCaseLikeAndIdNot(name,actualUser.getId());

                List<AddAssistant> addAssistants = assistantsrecords.stream()
                        .map(record -> new AddAssistant(record.getAssistant(), record.getStatus()))
                        .collect(Collectors.toList());

                List<AddAssistant> secondAssistants = userdetails.stream()
                        .filter(user1 -> addAssistants.stream().noneMatch(addAssistant -> addAssistant.getUser().getUsername().equals(user1.getUsername())))
                        .map(record -> new AddAssistant(record, "new"))
                        .collect(Collectors.toList());
                addAssistants.addAll(secondAssistants);
                return new ResponseEntity<>(addAssistants,HttpStatus.OK);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Send assist request
    public ResponseEntity<Object> sendRequest(String idToken,Userdetails user2) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
               Optional<Assist> assists = assistRepository.findByUser_UsernameAndAssistant_Username(actualUser.getUsername(),user2.getUsername());
               Assist assist = assists.orElse(null);
                if (assist!= null)
                {
                    // which means already some requests are there
                    if(assist.getStatus().equals("cancelled") || assist.getStatus().equals("rejected")) {
                        assist.setStatus("pending");
                    }
                    assistRepository.save(assist);
                    return new ResponseEntity<>(assist, HttpStatus.OK);
                }
                else
                {
                    Assist newassist = new Assist();
                    newassist.setUser(actualUser);
                    newassist.setAssistant(user2);
                    newassist.setStatus("pending");
                    assistRepository.save(newassist);
                    return new ResponseEntity<>(newassist, HttpStatus.OK);
                }
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Accept assist request
    public ResponseEntity<Object> acceptRequest(String idToken,Userdetails user2) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Assist> assists = assistRepository.findByAssistantAndUser(actualUser.getUsername(),user2.getUsername());
                Assist assist = assists.orElse(null);
                if(assist.getStatus().equals("pending")) {
                    assist.setStatus("accepted");
                    assistRepository.save(assist);
                    return new ResponseEntity<>(assist, HttpStatus.OK);
                }
                else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Cancel assist request
    public ResponseEntity<Object> cancelRequest(String idToken,Userdetails user2) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Assist> assists = assistRepository.findByUser_UsernameAndAssistant_Username(actualUser.getUsername(),user2.getUsername());
                Assist assist = assists.orElse(null);

                if(assist.getStatus().equals("pending")) {
                    assist.setStatus("cancelled");
                    assistRepository.save(assist);
                    return new ResponseEntity<>(assist, HttpStatus.OK);
                }
                else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            }
            else
            {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Reject assist request
    public ResponseEntity<Object> rejectRequest(String idToken,Userdetails user2) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Assist> assists = assistRepository.findByAssistantAndUser(actualUser.getUsername(),user2.getUsername());
                Assist assist = assists.orElse(null);
                if(assist.getStatus().equals("pending")) {
                    assist.setStatus("rejected");
                    assistRepository.save(assist);
                    return new ResponseEntity<>(assist, HttpStatus.OK);
                }
                else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Get all my assist requests
    public ResponseEntity<Object> getMyAssistRequests(String idToken) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Assist> assists = assistRepository.findByAssistantAndStatusContaining(actualUser,"pending");
                return new ResponseEntity<>(assists,HttpStatus.OK);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Get all people
    public ResponseEntity<Object> getMyPeople(String idToken) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Assist> assists = assistRepository.findByAssistantAndStatusContaining(actualUser,"accepted");
                return new ResponseEntity<>(assists,HttpStatus.OK);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Get my assistants
    public ResponseEntity<Object> getMyAssistants(String idToken) {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Assist> assists = assistRepository.findByUserAndStatusContaining(actualUser,"accepted");
                return new ResponseEntity<>(assists,HttpStatus.OK);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
