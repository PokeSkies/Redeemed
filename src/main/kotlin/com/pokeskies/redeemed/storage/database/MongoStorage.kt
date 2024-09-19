package com.pokeskies.redeemed.storage.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.connection.ClusterSettings
import com.pokeskies.redeemed.config.StorageSettings
import com.pokeskies.redeemed.data.CodeData
import com.pokeskies.redeemed.data.UserData
import com.pokeskies.redeemed.storage.IStorage
import com.pokeskies.redeemed.utils.UUIDCodec
import com.pokeskies.redeemed.utils.Utils
import org.bson.Document
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import java.io.IOException
import java.util.*

class MongoStorage(config: StorageSettings) : IStorage {
    private var mongoClient: MongoClient? = null
    private var mongoDatabase: MongoDatabase? = null

    private var userDataCollection: MongoCollection<Document>? = null
    private var codeDataCollection: MongoCollection<Document>? = null

    init {
        try {
            val credential = MongoCredential.createCredential(
                config.username,
                config.database,
                config.password.toCharArray()
            )
            var settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)

            settings = if (config.urlOverride.isNotEmpty()) {
                settings.applyConnectionString(ConnectionString(config.urlOverride))
            } else {
                settings
                    .credential(credential)
                    .applyToClusterSettings { builder: ClusterSettings.Builder ->
                        builder.hosts(listOf(ServerAddress(config.host, config.port)))
                    }
            }

            this.mongoClient = MongoClients.create(settings.build())

            val codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(UUIDCodec()),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )

            this.mongoDatabase = mongoClient!!.getDatabase(config.database)
                .withCodecRegistry(codecRegistry)
            this.userDataCollection = this.mongoDatabase!!.getCollection("userData")
            this.userDataCollection = this.mongoDatabase!!.getCollection("codeData")
        } catch (e: Exception) {
            throw IOException("Error while attempting to setup Mongo Database: $e")
        }
    }

    override fun getUser(uuid: UUID): UserData {
        if (mongoDatabase == null) {
            Utils.printError("There was an error while attempting to fetch data from the Mongo database!")
            return UserData()
        }
        val doc: Document? = userDataCollection?.find(Filters.eq("uuid", uuid.toString()))?.first()
        return if (doc != null) {
            UserData(doc.getList("usedCodes", String::class.java))
        } else {
            UserData()
        }
    }

    override fun saveUser(uuid: UUID, userData: UserData): Boolean {
        if (mongoDatabase == null) {
            Utils.printError("There was an error while attempting to save data to the Mongo database!")
            return false
        }
        val query = Filters.eq("uuid", uuid.toString())
        var doc: Document? = userDataCollection?.find(query)?.first()
        if (doc == null) {
            doc = Document()
        }
        doc["uuid"] = uuid.toString()
        doc["usedCodes"] = userData.usedCodes
        val result = this.userDataCollection?.replaceOne(query, doc, ReplaceOptions().upsert(true))

        return result?.wasAcknowledged() ?: false
    }

    override fun getCodeData(id: String): CodeData? {
        if (mongoDatabase == null) {
            Utils.printError("There was an error while attempting to fetch data from the Mongo database!")
            return null
        }
        val doc: Document? = codeDataCollection?.find(Filters.eq("_id", id))?.first()
        return if (doc != null) {
            CodeData(doc.getInteger("uses"))
        } else {
            null
        }
    }

    override fun saveCodeData(id: String, codeData: CodeData): Boolean {
        if (mongoDatabase == null) {
            Utils.printError("There was an error while attempting to save data to the Mongo database!")
            return false
        }
        val query = Filters.eq("_id", id)
        var doc: Document? = codeDataCollection?.find(query)?.first()
        if (doc == null) {
            doc = Document()
        }
        doc["_id"] = id
        doc["uses"] = codeData.uses
        val result = this.codeDataCollection?.replaceOne(query, doc, ReplaceOptions().upsert(true))

        return result?.wasAcknowledged() ?: false
    }

    override fun close() {
        mongoClient?.close()
    }
}
