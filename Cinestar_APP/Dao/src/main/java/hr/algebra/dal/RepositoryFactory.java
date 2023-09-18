package hr.algebra.dal;

import hr.algebra.dal.sql.SqlRepository;

public class RepositoryFactory {
    private RepositoryFactory() {
    }
    
    public static Repository getRepository() throws Exception {
        return new SqlRepository();
    }
}
