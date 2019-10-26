package org.alexdev.photoviewer.test.game;

import org.alexdev.photoviewer.test.Storage;

import java.sql.*;

public class Photo {
    public static Photo getPhoto(long photoId) throws SQLException {
        Photo photo = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM items_photos WHERE photo_id = ?", sqlConnection);// (photo_id, photo_user_id, timestamp, photo_data, photo_checksum) VALUES (?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setLong(1, photoId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Blob photoBlob = resultSet.getBlob("photo_data");
                int blobLength = (int) photoBlob.length();

                byte[] photoBlobBytes = photoBlob.getBytes(1, blobLength);
                photo = new Photo(photoId, resultSet.getInt("photo_checksum"), photoBlobBytes, resultSet.getLong("timestamp"));
            }

        } catch (Exception e) {
            Storage.logError(e);
            throw e;
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
            Storage.closeSilently(resultSet);
        }

        return photo;
    }

    private long databaseId;
    private int checksum;
    private byte[] data;
    private long time;

    public Photo(long id, int checksum, byte[] data, long time) {
        this.databaseId = id;
        this.checksum = checksum;
        this.data = data;
        this.time = time;
    }

    public long getId() {
        return databaseId;
    }

    public int getChecksum() {
        return checksum;
    }

    public byte[] getData() {
        return data;
    }

    public long getTime() {
        return time;
    }
}
