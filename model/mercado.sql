-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 24-Fev-2020 às 18:35
-- Versão do servidor: 10.1.30-MariaDB
-- PHP Version: 7.2.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mercado`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `ano`
--

CREATE TABLE `ano` (
  `ano` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `ano`
--

INSERT INTO `ano` (`ano`) VALUES
(2019);

-- --------------------------------------------------------

--
-- Estrutura da tabela `cliente`
--

CREATE TABLE `cliente` (
  `CPF` varchar(14) NOT NULL,
  `NOME` varchar(45) NOT NULL,
  `SOBRENOME` varchar(45) NOT NULL,
  `RUA` varchar(45) NOT NULL,
  `CIDADE` varchar(45) NOT NULL,
  `ESTADO` varchar(2) NOT NULL,
  `DATA_CADASTRO` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `cliente`
--

INSERT INTO `cliente` (`CPF`, `NOME`, `SOBRENOME`, `RUA`, `CIDADE`, `ESTADO`, `DATA_CADASTRO`) VALUES
('111.111.111-11', 'Cliente', 'Padrão', 'Indefinida', 'indefinida', 'xx', '2020-02-19');

-- --------------------------------------------------------

--
-- Estrutura da tabela `fornecedores`
--

CREATE TABLE `fornecedores` (
  `CNPJ` varchar(18) NOT NULL,
  `NOME` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `fornecedores`
--

INSERT INTO `fornecedores` (`CNPJ`, `NOME`) VALUES
('11.111.111/1111-11', 'Fornecedor padrão');

-- --------------------------------------------------------

--
-- Estrutura da tabela `itens`
--

CREATE TABLE `itens` (
  `VENDA_CODIGO` int(11) NOT NULL,
  `PRODUTOS_CODIGO` varchar(13) NOT NULL,
  `QUANTIDADE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------

--
-- Estrutura da tabela `loja`
--

CREATE TABLE `loja` (
  `CNPJ` varchar(18) NOT NULL,
  `NOME` varchar(100) NOT NULL,
  `RUA` varchar(45) NOT NULL,
  `CIDADE` varchar(45) NOT NULL,
  `ESTADO` varchar(2) NOT NULL,
  `CEP` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `produtos`
--

CREATE TABLE `produtos` (
  `CODIGO` varchar(13) NOT NULL,
  `DESCRICAO` varchar(800) NOT NULL,
  `QUANTIDADE` int(11) NOT NULL,
  `VALIDADE` date NOT NULL,
  `PRECO` double NOT NULL,
  `FORNECEDORES_CNPJ` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE `usuario` (
  `CPF` varchar(14) NOT NULL,
  `NOME_USER` varchar(45) DEFAULT NULL,
  `SENHA` varchar(45) DEFAULT NULL,
  `EMAIL` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `usuario`
--

INSERT INTO `usuario` (`CPF`, `NOME_USER`, `SENHA`, `EMAIL`) VALUES
('111.111.111-11', 'USER PADRÃO', '123456', 'USER@USER.COM');

-- --------------------------------------------------------

--
-- Estrutura da tabela `venda`
--

CREATE TABLE `venda` (
  `CODIGO` int(11) NOT NULL,
  `DATA` datetime DEFAULT NULL,
  `CLIENTE_CPF` varchar(14) NOT NULL,
  `PAGO` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Indexes for dumped tables
--

--
-- Indexes for table `ano`
--
ALTER TABLE `ano`
  ADD PRIMARY KEY (`ano`);

--
-- Indexes for table `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`CPF`);

--
-- Indexes for table `fornecedores`
--
ALTER TABLE `fornecedores`
  ADD PRIMARY KEY (`CNPJ`);

--
-- Indexes for table `itens`
--
ALTER TABLE `itens`
  ADD KEY `fk_VENDA_has_PRODUTOS_PRODUTOS1_idx` (`PRODUTOS_CODIGO`),
  ADD KEY `fk_VENDA_has_PRODUTOS_VENDA_idx` (`VENDA_CODIGO`);

--
-- Indexes for table `loja`
--
ALTER TABLE `loja`
  ADD PRIMARY KEY (`CNPJ`);

--
-- Indexes for table `produtos`
--
ALTER TABLE `produtos`
  ADD PRIMARY KEY (`CODIGO`),
  ADD KEY `fk_PRODUTOS_FORNECEDORES1_idx` (`FORNECEDORES_CNPJ`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`CPF`);

--
-- Indexes for table `venda`
--
ALTER TABLE `venda`
  ADD PRIMARY KEY (`CODIGO`),
  ADD KEY `fk_VENDA_CLIENTE1_idx` (`CLIENTE_CPF`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `venda`
--
ALTER TABLE `venda`
  MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `itens`
--
ALTER TABLE `itens`
  ADD CONSTRAINT `fk_VENDA_has_PRODUTOS_PRODUTOS1` FOREIGN KEY (`PRODUTOS_CODIGO`) REFERENCES `produtos` (`CODIGO`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_VENDA_has_PRODUTOS_VENDA` FOREIGN KEY (`VENDA_CODIGO`) REFERENCES `venda` (`CODIGO`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `produtos`
--
ALTER TABLE `produtos`
  ADD CONSTRAINT `fk_PRODUTOS_FORNECEDORES1` FOREIGN KEY (`FORNECEDORES_CNPJ`) REFERENCES `fornecedores` (`CNPJ`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `venda`
--
ALTER TABLE `venda`
  ADD CONSTRAINT `fk_VENDA_CLIENTE1` FOREIGN KEY (`CLIENTE_CPF`) REFERENCES `cliente` (`CPF`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
