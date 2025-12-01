CREATE DATABASE IF NOT EXISTS PsyWare2;
USE PsyWare2;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(50) UNIQUE,
    senha VARCHAR(100),
    cargo VARCHAR(30)
);

INSERT INTO usuarios (nome, email, senha, cargo)
VALUES ('Administrador', 'root', 'root', 'admin')
ON DUPLICATE KEY UPDATE senha='root';

CREATE TABLE IF NOT EXISTS produtos (
    id_produto INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(150),
    descricao_produto TEXT,
    quantidade_estoque INT,
    preco DECIMAL(10,2),
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    categoria VARCHAR(50) DEFAULT 'Diversos',
    imagem VARCHAR(255)
);

INSERT INTO produtos (nome_produto, descricao_produto, quantidade_estoque, preco, categoria, imagem) VALUES
('Mouse Logitech MX Master 3S', 'Ergonômico, 8000 DPI, silencioso', 32, 649.90, 'Mouse', 'mx_master_3s.jpg'),
('Mouse Razer DeathAdder V3 Pro', '63g, 30K DPI, HyperSpeed', 18, 1199.90, 'Mouse', 'deathadder_v3_pro.jpg'),
('Mouse Logitech G Pro X Superlight 2', '60g, HERO 2 sensor', 25, 1099.90, 'Mouse', 'superlight2.jpg'),
('Teclado Keychron K8 Pro QMK', 'Hot-swap, RGB, Bluetooth', 40, 749.90, 'Teclado', 'k8_pro.jpg'),
('Teclado Akko 3087 DS Horizon', 'Cherry MX Brown, PBT', 22, 529.90, 'Teclado', 'akko_3087.jpg'),
('Teclado NuPhy Air75 V2', 'Low profile, RGB, alumínio', 15, 899.90, 'Teclado', 'nuphy_air75.jpg'),
('Monitor Samsung Odyssey G7 32"', '1000R, 240Hz, QLED', 8, 3499.90, 'Monitor', 'odyssey_g7.jpg'),
('Monitor LG UltraGear 27GP950', '4K 144Hz, Nano IPS', 5, 4299.90, 'Monitor', 'lg_27gp950.jpg'),
('Monitor Dell Alienware AW3423DW', '34" QD-OLED Curvo 175Hz', 3, 9499.90, 'Monitor', 'aw3423dw.jpg'),
('Headset HyperX Cloud Alpha', 'Dual Chamber, 50mm', 45, 499.90, 'Headset', 'cloud_alpha.jpg'),
('Headset SteelSeries Arctis Nova Pro Wireless', 'ANC, base station', 12, 2199.90, 'Headset', 'arctis_nova_pro.jpg');

