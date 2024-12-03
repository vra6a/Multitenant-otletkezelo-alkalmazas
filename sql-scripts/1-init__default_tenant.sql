CREATE SCHEMA IF NOT EXISTS default_tenant;

CREATE TABLE IF NOT EXISTS default_tenant.user (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) DEFAULT NULL,
    `firstName` VARCHAR(255) DEFAULT NULL,
    `lastName` VARCHAR(255) DEFAULT NULL,
    `password` VARCHAR(255) DEFAULT NULL,
    `role` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.ideabox (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(255) DEFAULT NULL,
    `end_date` DATETIME DEFAULT NULL,
    `is_sclosed` BIT(1) NOT NULL,
    `name` VARCHAR(255) DEFAULT NULL,
    `start_date` DATETIME DEFAULT NULL,
    `user_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FKfl3suoghrss86b0d8hpy9plmt (user_id),
    CONSTRAINT FKfl3suoghrss86b0d8hpy9plmt FOREIGN KEY (user_id) REFERENCES default_tenant.user (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.idea (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `creation_date` datetime DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL,
    `title` varchar(255) DEFAULT NULL,
    `idea_box_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKq1fshkaxm10vbry80i87x485u` (`idea_box_id`),
    KEY `FKcbrpauo6w2avi3eoaqtfpoxov` (`user_id`),
    CONSTRAINT `FKcbrpauo6w2avi3eoaqtfpoxov` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKq1fshkaxm10vbry80i87x485u` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.idea_juries (
    `idea_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    KEY `FK3ib0hproe0he1nfbbyu3mt4w4` (`user_id`),
    KEY `FK7al5i6mntp3l74kunqbq9hanj` (`idea_id`),
    CONSTRAINT `FK3ib0hproe0he1nfbbyu3mt4w4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK7al5i6mntp3l74kunqbq9hanj` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.idea_likes (
    `idea_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    KEY `FKk6ogm4kd7bdd7kjk9qkv9n4oc` (`user_id`),
    KEY `FK3nooi3gnrtblxgajdytj3ikcw` (`idea_id`),
    CONSTRAINT `FK3nooi3gnrtblxgajdytj3ikcw` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
    CONSTRAINT `FKk6ogm4kd7bdd7kjk9qkv9n4oc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.tag (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.required_juries (
    `idea_box_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    KEY `FKl25fs18lltuf8n869hwir4ki2` (`user_id`),
    KEY `FKbvc217u6gcao0124upfc2k692` (`idea_box_id`),
    CONSTRAINT `FKbvc217u6gcao0124upfc2k692` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`),
    CONSTRAINT `FKl25fs18lltuf8n869hwir4ki2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.score_sheet (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `idea_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    `idea_box_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKfe1hd7rbguy9339ayvopgmamu` (`idea_id`),
    KEY `FKq7ufumpw2ri4w174ncxvj3s79` (`user_id`),
    KEY `FKkfc65uts5155sorlcfnab7rxw` (`idea_box_id`),
    CONSTRAINT `FKfe1hd7rbguy9339ayvopgmamu` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
    CONSTRAINT `FKkfc65uts5155sorlcfnab7rxw` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`),
    CONSTRAINT `FKq7ufumpw2ri4w174ncxvj3s79` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.score_item (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `score` int DEFAULT NULL,
    `text` varchar(255) DEFAULT NULL,
    `title` varchar(255) DEFAULT NULL,
    `type` varchar(255) DEFAULT NULL,
    `score_sheet_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK3y8mqcb3cw70oxv1274jvif6n` (`score_sheet_id`),
    CONSTRAINT `FK3y8mqcb3cw70oxv1274jvif6n` FOREIGN KEY (`score_sheet_id`) REFERENCES `score_sheet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.comment (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `creation_date` datetime DEFAULT NULL,
    `is_edited` bit(1) NOT NULL,
    `text` varchar(255) DEFAULT NULL,
    `idea_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKpo553b3rappx4h6o9lb6lr7xy` (`idea_id`),
    KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
    CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKpo553b3rappx4h6o9lb6lr7xy` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.comment_likes (
    `comment_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    KEY `FKgtjsp4k7rsoon6lnxjjx7cnqp` (`user_id`),
    KEY `FKd0epu3dcjc57pwe7lt5jgfqsi` (`comment_id`),
    CONSTRAINT `FKd0epu3dcjc57pwe7lt5jgfqsi` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
    CONSTRAINT `FKgtjsp4k7rsoon6lnxjjx7cnqp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS default_tenant.idea_tags (
    `idea_id` bigint NOT NULL,
    `tag_id` bigint NOT NULL,
    KEY `FKcw5ikpmc6lgu7ykgj2u30suuc` (`tag_id`),
    KEY `FK3gt86actmlp7683x3buwfakb4` (`idea_id`),
    CONSTRAINT `FK3gt86actmlp7683x3buwfakb4` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
    CONSTRAINT `FKcw5ikpmc6lgu7ykgj2u30suuc` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;