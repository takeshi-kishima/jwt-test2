package takeshi.kishima.jwttest2.user;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Data
    public static class UserForm {
        private String username;
        private String password;
    }

    private final ApplicationUserRepository applicationUserRepository; //finalをつけること @RequiredArgsConstructorアノテーションをクラスに指定するとfinalなフィールドを初期化するコンストラクタが生成されます。Spring4.3以降からは単一のコンストラクタの場合、@Autowired自体が不要になります。

    @PostMapping("/sign-up")
    public void signUp(@RequestBody final UserForm form) {
        final ApplicationUser user = new ApplicationUser(
            form.getUsername(),
            form.getPassword());

        final ApplicationUser saved = applicationUserRepository.save(user);

        log.info("User sign-upped: {}", saved);
    }

    @GetMapping("")
    public List<ApplicationUser> users() {
        return applicationUserRepository.findAll();
    }
}
