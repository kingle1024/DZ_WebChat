CREATE TABLE `board_popularity` (
                                    `bno` int DEFAULT NULL,
                                    `userId` varchar(100) DEFAULT NULL,
                                    `type` varchar(10) DEFAULT NULL,
                                    `register_date` datetime DEFAULT NULL,
                                    `isDelete` tinyint DEFAULT NULL,
                                    KEY `foreign_popular_bno` (`bno`),
                                    CONSTRAINT `foreign_popular_bno` FOREIGN KEY (`bno`) REFERENCES `boards` (`bno`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `boards` (
                          `bno` int NOT NULL AUTO_INCREMENT,
                          `btitle` varchar(100) NOT NULL,
                          `bcontent` longtext NOT NULL,
                          `bwriter` varchar(50) NOT NULL,
                          `bdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `bhit` int DEFAULT NULL,
                          `bwriterId` varchar(100) DEFAULT NULL,
                          `type` varchar(100) DEFAULT NULL,
                          `isDelete` tinyint DEFAULT NULL,
                          `password` varchar(100) DEFAULT NULL,
                          PRIMARY KEY (`bno`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `del_board` (
                             `bno` int NOT NULL,
                             `del_date` datetime DEFAULT NULL,
                             UNIQUE KEY `bno` (`bno`),
                             CONSTRAINT `fk_del_bno` FOREIGN KEY (`bno`) REFERENCES `boards` (`bno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `MEMBER` (
                          `userId` varchar(100) NOT NULL,
                          `pwd` varchar(100) DEFAULT NULL,
                          `name` varchar(100) DEFAULT NULL,
                          `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                          `phone` varchar(100) DEFAULT NULL,
                          `createdate` datetime DEFAULT CURRENT_TIMESTAMP,
                          `logindatetime` datetime DEFAULT NULL,
                          `delete_yn` varchar(100) DEFAULT NULL,
                          `isAdmin` tinyint DEFAULT NULL,
                          `userStatus` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

