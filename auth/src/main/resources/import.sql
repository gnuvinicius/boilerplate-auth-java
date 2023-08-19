INSERT INTO
  tb_role (role_id, role_name)
VALUES
('0d39a2f4-07be-4d72-ac7a-9c6f3b6846ce', 'ROLE_ROOT');

INSERT INTO
  tb_role (role_id, role_name)
VALUES
('64e4b21e-166c-4068-b1ea-4fc1f9d3c2c7', 'ROLE_ADMIN');

INSERT INTO
  tb_role (role_id, role_name)
VALUES
('7de7ec5c-74ad-4dcd-a4ba-7b72b3d8c83f', 'ROLE_USER');

INSERT INTO
  tb_usuario (
    usuario_id,
    status,
    nome,
    email,
    "password",
    primeiro_acesso,
    ultimo_acesso,
    token_refresh_password,
    token_refresh_password_valid
  )
VALUES
(
    'e26c18d5-8fa0-4ad2-afaf-2fa3122e2a5b',
    1,
    'root',
    'root@dashboard.dev',
    '$2a$10$8XLp.udJ.4heVUyVGLi5A.m0pkLflJmC89VRKL7Bsb088yGyXvo4K',
    false,
    null,
    false,
    false
  );

INSERT INTO
  tb_user_role (user_id, role_id)
VALUES
  (
    'e26c18d5-8fa0-4ad2-afaf-2fa3122e2a5b',
    '0d39a2f4-07be-4d72-ac7a-9c6f3b6846ce'
  );