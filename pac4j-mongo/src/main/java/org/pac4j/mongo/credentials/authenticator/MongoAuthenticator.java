package org.pac4j.mongo.credentials.authenticator;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.util.CommonHelper;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Authenticator for users stored in a MongoDB database, based on the {@link MongoClient} class from the Java Mongo
 * driver.
 *
 * Add the <code>spring-security-crypto</code> dependency to use this class.
 *
 * @author Victor Noël
 * @since 1.9.2
 */
public class MongoAuthenticator extends AbstractMongoAuthenticator<Document> {

    protected MongoClient mongoClient;

    public MongoAuthenticator() {
    }

    public MongoAuthenticator(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoAuthenticator(final MongoClient mongoClient, final String attributes) {
        super(attributes);
        this.mongoClient = mongoClient;
    }

    public MongoAuthenticator(final MongoClient mongoClient, final String attributes,
            final PasswordEncoder passwordEncoder) {
        super(attributes, passwordEncoder);
        this.mongoClient = mongoClient;
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("mongoClient", this.mongoClient);

        super.internalInit(context);
    }

    @Override
    protected Iterable<Document> getUsersFor(UsernamePasswordCredentials credentials) {
        final MongoDatabase db = mongoClient.getDatabase(usersDatabase);
        final MongoCollection<Document> collection = db.getCollection(usersCollection);

        return collection.find(eq(usernameAttribute, credentials.getUsername()));
    }

    @Override
    protected String getUserAttribute(Document user, String attribute) {
        return user.getString(attribute);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
}
