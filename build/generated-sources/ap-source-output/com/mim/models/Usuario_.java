package com.mim.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuario.class)
public abstract class Usuario_ {

	public static volatile SingularAttribute<Usuario, Integer> quintana;
	public static volatile SingularAttribute<Usuario, Integer> tabasco;
	public static volatile SingularAttribute<Usuario, String> usuario;
	public static volatile SingularAttribute<Usuario, String> contrasena;
	public static volatile SingularAttribute<Usuario, String> nombreCompleto;
	public static volatile ListAttribute<Usuario, TipoUsuario> tipoUsuarioList;
	public static volatile SingularAttribute<Usuario, Integer> idusuario;

}

