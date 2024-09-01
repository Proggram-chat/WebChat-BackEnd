INSERT INTO functions(id, action) VALUES (0, 'DELETE_CHAT'), (1, 'REMOVE_MEMBER'), (2, 'MODIFY_CHAT'), (3, 'RESTRICT_MEMBER'), (4, 'SEND_ATTACHMENTS'), (5, 'SEND_MESSAGE'), (6, 'MODIFY_MESSAGE');
INSERT INTO chats (id, description, name, type) VALUES('379ea606-92b7-46a8-a727-2faa3930f31f', 'chat to demonstrate functionality', 'test-chat', 'GROUP'), ('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'General discussion chat', 'General', 'GROUP'),('a7dbe64d-4a4f-4d5a-b8b9-6205e6e8cfc3', 'Project X team chat', 'Project X', 'GROUP'),('b36c1bcb-fd5c-431e-9cf7-1a4ab6f3d631', 'Announcements channel', 'Announcements', 'CHANNEL'),('c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', 'Direct message chat', 'Direct Chat', 'DIRECT');
INSERT INTO members (id, description, nickname, connected_at) VALUES('f5ccbd0f-4795-4cf8-8a12-eb356e338b5d', 'Some test chat', 'Nickname', CURRENT_TIMESTAMP),('e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', 'Team lead of Project X', 'Alice', '2024-01-01 10:00:00+00'),('f1d5c833-8e6b-4e8a-8e9a-918b5e3d5b9b', 'Backend developer', 'Bob', '2024-01-01 10:05:00+00'),('d2b7f833-0f6b-4e9a-8f9a-918c5e3d5c9b', 'Frontend developer', 'Charlie', '2024-01-01 10:10:00+00'),('c3a8f833-1e6b-4e9a-9f9a-918d5e3d5d9b', 'DevOps engineer', 'Dave', '2024-01-01 10:15:00+00');
INSERT INTO member_roles (chat_id, id, role, functions) VALUES ('379ea606-92b7-46a8-a727-2faa3930f31f', 'e7a9e1a6-16f5-4c1f-9b7a-1f2c23a8dba0', 'ADMIN', '0,1,2,3,4,5,6'), ('379ea606-92b7-46a8-a727-2faa3930f31f', 'b36cb85c-3a3e-497f-9c5d-4e935c85fcf9', 'MEMBER', '4,5,6'),('a7dbe64d-4a4f-4d5a-b8b9-6205e6e8cfc3', '5a9e1234-56f5-4d6a-8b6b-1c5c72b9dbd1', 'ADMIN', '0,1,2,3,4,5,6'),('a7dbe64d-4a4f-4d5a-b8b9-6205e6e8cfc3', '7b6e12b7-7890-4c2a-9c5e-3e5d82c9f2d7', 'MEMBER', '4,5,6'),('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', '8c9f65d7-19a4-4f3a-8c4d-6d7e95b9fb25', 'ADMIN', '0,1,2,3,4,5,6'),('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', '9d5e17a3-5b6c-4f9e-9b3a-7c2d34e1fa19', 'MEMBER', '4,5,6'),('b36c1bcb-fd5c-431e-9cf7-1a4ab6f3d631', '1a2b345c-67d8-49f7-9a6b-2b8d95e7fa29', 'ADMIN', '0,1,2,3,4,5,6'),('b36c1bcb-fd5c-431e-9cf7-1a4ab6f3d631', '3b4e76f9-8c2d-49fa-9d3a-5f7e89b2d1c3', 'MEMBER', '4,5,6'),('c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', '4c7d89a2-1b2c-49e7-8a5f-6c3d89e2b1d8', 'ADMIN', '0,1,2,3,4,5,6'),('c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', '5d8e91b3-2c4d-49e8-9d5f-7b4e92d3c1d7', 'MEMBER', '4,5,6'), ('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'f2d6fbeb-c4fa-4328-a611-20febbfb37e9', 'MODERATOR', '0,1,2,3,4,5');
INSERT INTO chat_members (chat_id, member_id, member_role_id) VALUES('379ea606-92b7-46a8-a727-2faa3930f31f', 'f5ccbd0f-4795-4cf8-8a12-eb356e338b5d', 'e7a9e1a6-16f5-4c1f-9b7a-1f2c23a8dba0'), ('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', '8c9f65d7-19a4-4f3a-8c4d-6d7e95b9fb25'),('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'f1d5c833-8e6b-4e8a-8e9a-918b5e3d5b9b', '9d5e17a3-5b6c-4f9e-9b3a-7c2d34e1fa19'),('d1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'd2b7f833-0f6b-4e9a-8f9a-918c5e3d5c9b', '9d5e17a3-5b6c-4f9e-9b3a-7c2d34e1fa19'),('a7dbe64d-4a4f-4d5a-b8b9-6205e6e8cfc3', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', '5a9e1234-56f5-4d6a-8b6b-1c5c72b9dbd1'),('a7dbe64d-4a4f-4d5a-b8b9-6205e6e8cfc3', 'f1d5c833-8e6b-4e8a-8e9a-918b5e3d5b9b', '7b6e12b7-7890-4c2a-9c5e-3e5d82c9f2d7'),('b36c1bcb-fd5c-431e-9cf7-1a4ab6f3d631', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', '1a2b345c-67d8-49f7-9a6b-2b8d95e7fa29'),('c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', '4c7d89a2-1b2c-49e7-8a5f-6c3d89e2b1d8'),('c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', 'f1d5c833-8e6b-4e8a-8e9a-918b5e3d5b9b', '5d8e91b3-2c4d-49e8-9d5f-7b4e92d3c1d7');
INSERT INTO messages (id, chat_id, sender_id, content, received_at) VALUES('a1e5f99d-758f-4ebc-8b9d-48cb85b1b111', 'd1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', 'Hello team!', '2024-01-01 10:30:00+00'),('b2d5c833-8e6b-4e8a-8e9a-918b5e3d5b22', 'd1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'f1d5c833-8e6b-4e8a-8e9a-918b5e3d5b9b', 'Hi Alice!', '2024-01-01 10:35:00+00'),('c3b7f833-0f6b-4e9a-8f9a-918c5e3d5c33', 'd1e5f99d-758f-4ebc-8b9d-48cb85b1b153', 'd2b7f833-0f6b-4e9a-8f9a-918c5e3d5c9b', 'Good morning everyone!', '2024-01-01 10:40:00+00'),('d4a8f833-1e6b-4e9a-9f9a-918d5e3d5d44', 'a7dbe64d-4a4f-4d5a-b8b9-6205e6e8cfc3', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', 'Project X update: We are on track.', '2024-01-01 10:45:00+00'),('479ea606-91b7-42a8-a727-2faa3730f31f', 'b36c1bcb-fd5c-431e-9cf7-1a4ab6f3d631', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', 'Reminder: Company meeting at 2 PM.', '2024-01-01 10:50:00+00'),('f6d2f99d-758f-4ebc-8b9d-48cb85b1b666', 'c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', 'e0afeb8b-307e-4a4d-a8f4-9c9f5e34b2b3', 'Hey Bob, how are you?', '2024-01-01 10:55:00+00'),('a7e3c833-8e6b-4e8a-8e9a-918b5e3d5b77', 'c04eacda-2d6e-4f98-bbc2-3fbcad3e81c5', 'f1d5c833-8e6b-4e8a-8e9a-918b5e3d5b9b', 'I am good, Alice. Thanks for asking!', '2024-01-01 11:00:00+00');