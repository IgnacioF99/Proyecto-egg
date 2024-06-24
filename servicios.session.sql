CREATE DATABASE dbservicios;

use dbservicios;

-- update usuario set rol = 'ADMIN' where nombre = 'Abrahan';

-- update usuario set rol = 'PROV' where nombre = 'David';

-- INSERT INTO provedor (alta, calificacion_promedio, numero_de_trabajos, usuario_id) values (true, 4.3,2,3);

-- INSERT into servicios_has_provedores (provedor_id,servicio_id) values (1,6);

show create table comentario;

ALTER table trabajo modify column estado ENUM('SOLICITADO','ACEPTADO','PRESUPUESTADO', 'TERMINADO','CANCELADO');

alter TABLE trabajo modify column presupuesto DOUBLE;

-- update trabajo set estado = 'SOLICITADO' where descripcion = 'primer trabajo';

alter table comentario drop foreign key FKsnw2c50yyvxqymdphrng9ee2g;

drop table comentario;