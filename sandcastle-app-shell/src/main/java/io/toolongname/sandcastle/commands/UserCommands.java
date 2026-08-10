package io.toolongname.sandcastle.commands;

import io.toolongname.sandcastle.entity.bo.user.ActiveCodeBO;
import io.toolongname.sandcastle.services.UserService;
import org.springframework.shell.core.command.CommandContext;
import org.springframework.shell.core.command.annotation.Argument;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.CommandGroup;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.List;

@Component
@CommandGroup(name = "User Manager Commands", description = "用户管理", prefix = "user")
public class UserCommands {
    private final UserService userService;

    public UserCommands(UserService userService) {
        this.userService = userService;
    }

    @Command(name = "active-code gen", description = "生成一个激活码")
    public void generateActiveCode(CommandContext context) throws Exception {
        String code = userService.generateActiveCode();

        context.outputWriter().println(code);
        context.outputWriter().flush();
    }

    @Command(name = "active-code batch-gen", description = "批量生成激活码")
    public void batchGenerateActiveCode(CommandContext context, @Argument(index = 0, defaultValue = "10", description = "数量") int batchSize) throws Exception {
        List<String> codeList = userService.batchGenerateActiveCode(batchSize);

        printActiveCodeCsv(context.outputWriter(), codeList);
    }

    @Command(name = "active-code", description = "列出可用的激活码")
    public void listAvailableActiveCode(CommandContext context) throws Exception {
        List<ActiveCodeBO> activeCodeList = userService.listAvailableActiveCode();

        printActiveCodeCsv(context.outputWriter(), activeCodeList.stream().map(ActiveCodeBO::code).toList());
    }

    private void printActiveCodeCsv(PrintWriter writer, List<String> codeList) throws Exception {
        for (String code : codeList) {
            writer.println(code);
        }
    }
}
