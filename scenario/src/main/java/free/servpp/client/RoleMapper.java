package free.servpp.client;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public interface RoleMapper {
    void roleToEntity(Role role, Entity entity);

    void entityToRole(Entity entity,Role role);
}
