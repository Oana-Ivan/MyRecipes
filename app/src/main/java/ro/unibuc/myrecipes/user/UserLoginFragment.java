package ro.unibuc.myrecipes.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import ro.unibuc.myrecipes.R;
import ro.unibuc.myrecipes.models.User;
import ro.unibuc.myrecipes.recipes.AllRecipesFragment;
import ro.unibuc.myrecipes.user.room.AppDatabase;
import ro.unibuc.myrecipes.user.room.UserDao;

public class UserLoginFragment extends Fragment {
    public static final String USER_PREFERENCES = "UserPreferences";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private EditText usernameET, passwordET;
    private CheckBox newUserCB;
    private AppCompatButton loginBtn;
    private String username, password;

    AppDatabase roomDB;
    UserDao userDao;
    List<User> users;

    public UserLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        initViews(view);

        loginBtn.setOnClickListener(btn -> {
            assignData();
            initDB();
            if (newUserCB.isChecked()) {
                User existingUser = userDao.findByUsername(username);
                if (existingUser == null) {
                    // save new user
                    int noOfUsers = userDao.getSize();
                    String newUserId = "user_" + (noOfUsers++);
                    User newUser = new User(newUserId, username, password);
                    userDao.insertAll(newUser);

                    saveCredentialsInSharedPreferences();
                    redirectToUserPage();
                }
                else {
                    Toast.makeText(getContext(), "Username taken", Toast.LENGTH_SHORT).show();
                    usernameET.setText("");
                }
            }
            else {
                // check credentials
                User existingUser = userDao.findByUsernameAndPassword(username, password);
                if (existingUser != null) {
                    saveCredentialsInSharedPreferences();
                    redirectToUserPage();
                }
                else {
                    Toast.makeText(getContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
                    usernameET.setText("");
                    passwordET.setText("");
                }
            }
        });

        return view;
    }

    private void redirectToUserPage() {
        // Redirect to user page
        UserFragment userPageFragment = new UserFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, userPageFragment);
        ft.commit();
    }

    private void saveCredentialsInSharedPreferences() {
        // Save username and user role in shared preferences
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    private void initDB() {
        roomDB = Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        userDao = roomDB.userDao();
        users = userDao.getAll();
    }

    private void assignData() {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
    }

    private void initViews(View view) {
        usernameET = view.findViewById(R.id.fragment_login_et_username);
        passwordET = view.findViewById(R.id.fragment_login_et_password);
        newUserCB = view.findViewById(R.id.fragment_login_cb_new_user);
        loginBtn = view.findViewById(R.id.fragment_login_btn_login);
    }
}