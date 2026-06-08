package Logica;

public enum RolUsuario {
    ADMINISTRADOR(1),
    INGENIERO(2),
    VISITANTE(3);

    private final int id;

    // Constructor privado (solo se usa aquí dentro)
    RolUsuario(int id) {
        this.id = id;
    }

    // Método para obtener el ID cuando guardes en BD
    public int getId() {
        return id;
    }

    // Método para convertir el INT de la BD a un Enum (¡Este es el clave!)
    public static RolUsuario fromId(int id) {
        for (RolUsuario rol : values()) {
            if (rol.getId() == id) return rol;
        }
        return VISITANTE; // Valor por defecto si el ID no existe
    }


}
