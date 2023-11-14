package free.servpp.client;

/**
 * @author lidong@date 2023-10-31@version 1.0
 * Semantic service client
 */
public interface AtomicService {
    default String getServiceName(){return null;}
//    default IConstance.ServiceType getServiceType(){return IConstance.ServiceType.unknown;}
    default Role[] getCallParameters(){return null;}

    void copyFrom(Role[] roles);
}
