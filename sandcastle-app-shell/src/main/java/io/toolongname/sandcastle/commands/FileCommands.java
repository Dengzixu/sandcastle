package io.toolongname.sandcastle.commands;

import io.toolongname.sandcastle.entity.bo.file.FileBO;
import io.toolongname.sandcastle.services.FileService;
import io.toolongname.sandcastlecommon.misc.constant.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.command.AbstractCommand;
import org.springframework.shell.core.command.CommandContext;
import org.springframework.shell.core.command.annotation.Argument;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.CommandGroup;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.shell.jline.tui.component.view.TerminalUI;
import org.springframework.shell.jline.tui.component.view.TerminalUIBuilder;
import org.springframework.shell.jline.tui.component.view.control.BoxView;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@CommandGroup(name = "File Manager Commands", description = "文件管理", prefix = "file")
public class FileCommands {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCommands.class);
    private final FileService fileService;

    public FileCommands(FileService fileService) {
        this.fileService = fileService;
    }

    @Command(name = "expired", description = "列出过期文件")
    public void listExpireFile(CommandContext context) {
        List<FileBO> fileBOList = fileService.findExpireFile()
                .stream()
                .toList();

        this.printFileListCsv(context.outputWriter(), fileBOList);
    }

    @Command(name = "available", description = "列出可用的文件")
    public void listAvailableFile(CommandContext context) {
        List<FileBO> fileBOList = fileService.findAvailable()
                .stream()
                .filter(fileBO -> {
                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(TimeZone.ASIA_SHANGHAI));
                    return fileBO.expireTimestamp() > now.toEpochSecond();
                })
                .toList();

        this.printFileListCsv(context.outputWriter(), fileBOList);
    }

    @Command(name = "mark", description = "标记文件为过期")
    public void markExpireFile(CommandContext context) {
        List<FileBO> fileBOList = fileService.findExpireFile()
                .stream()
                .filter(fileBO -> !fileService.isDeleted(fileBO.status()))
                .filter(fileBO -> !fileService.isExpired(fileBO.status()))
                .filter(fileBO -> {
                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(TimeZone.ASIA_SHANGHAI));
                    return fileBO.expireTimestamp() <= now.toEpochSecond();
                })
                .toList();

        if (fileBOList.isEmpty()) {
            LOGGER.warn("没有需要标记的文件。");
            return;
        }

        fileBOList.forEach(fileBO -> {
            fileService.markExpire(fileBO.id());
            LOGGER.info("文件 id: {}, UUID: {}, 标题: {} 已标记为过期。", fileBO.id(), fileBO.uuid(), fileBO.title());
        });
    }

    private void printFileListCsv(PrintWriter writer, List<FileBO> fileBOList) {
        fileBOList.forEach(fileBO -> {
            StringBuilder builder = new StringBuilder();


            String expiredTime = Instant.ofEpochSecond(fileBO.expireTimestamp())
                    .atZone(ZoneId.of(TimeZone.ASIA_SHANGHAI))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);


            writer.format("%-4s, %s, %s, %s, %s",
                    fileBO.id(),
                    fileBO.uuid().toString()
                    , fileBO.title(),
                    fileService.isExpired(fileBO.status()),
                    expiredTime);

            writer.println();

        });

        writer.flush();
    }
}
