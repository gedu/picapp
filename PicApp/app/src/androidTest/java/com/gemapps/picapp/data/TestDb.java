package com.gemapps.picapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gemapps.picapp.networking.FlickerUserClient;
import com.gemapps.picapp.networking.FlickrPhotosClient;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by edu on 10/5/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestDb {

    private static final String TAG = "TestDb";

    @Before
    public void setup(){

        deleteDatabase();
    }

    private void deleteDatabase(){

        getContext().deleteDatabase(PicSqlHelper.DATABASE_NAME);
    }

    @Test
    public void testUserTable(){
        UserItem userItem = getUserItem();

        assertNotNull(userItem);

        PicSqlHelper helper = new PicSqlHelper(getContext());
        SQLiteDatabase insertDb = helper.getWritableDatabase();

        ContentValues contentValues = PicappContract.UserEntry.parse(userItem);

        long id = insertDb.insert(PicappContract.UserEntry.TABLE_NAME, null, contentValues);

        assertNotEquals(-1, id);

        SQLiteDatabase readDb = helper.getReadableDatabase();

        Cursor cursor = readDb.query(PicappContract.UserEntry.TABLE_NAME,
                null,
                PicappContract.UserEntry._ID + "= ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        assertNotNull(cursor);

        cursor.moveToFirst();
        String userId = cursor.getString(cursor.getColumnIndex(PicappContract.UserEntry.COLUMN_USER_ID));
        String userNsid = cursor.getString(cursor.getColumnIndex(PicappContract.UserEntry.COLUMN_USER_NSID));

        assertEquals(userItem.getId(), userId);
        assertEquals(userItem.getNsid(), userNsid);

        cursor.close();
        insertDb.close();
        readDb.close();
    }

    @Test
    public void testPublicationTable(){
        PicItem picItem = getPicItem();

        assertNotNull(picItem);

        PicSqlHelper helper = new PicSqlHelper(getContext());
        SQLiteDatabase insertDb = helper.getWritableDatabase();

        ContentValues contentValues = PicappContract.PublicationEntry.parse(picItem);

        long id = insertDb.insert(PicappContract.PublicationEntry.TABLE_NAME, null, contentValues);

        assertNotEquals(-1, id);

        SQLiteDatabase readDb = helper.getReadableDatabase();

        Cursor cursor = readDb.query(PicappContract.PublicationEntry.TABLE_NAME,
                null,
                PicappContract.PublicationEntry._ID + "= ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        assertNotNull(cursor);

        cursor.moveToFirst();

        String ownerId = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_OWNER_ID));
        String picId = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_PIC_ID));

        assertEquals(picItem.getOwnerId(), ownerId);
        assertEquals(picItem.getPicId(), picId);

        cursor.close();
        insertDb.close();
        readDb.close();
    }

    @Test
    public void testBookmarkTable(){

        PicItem picItem = getPicItem();
        UserItem userItem = getUserItem();

        assertNotNull(picItem);
        assertNotNull(userItem);

        PicSqlHelper helper = new PicSqlHelper(getContext());
        SQLiteDatabase insertDb = helper.getWritableDatabase();

        ContentValues uContentValues = PicappContract.UserEntry.parse(userItem);
        ContentValues pContentValues = PicappContract.PublicationEntry.parse(picItem);

        long userId = insertDb.insert(PicappContract.UserEntry.TABLE_NAME, null, uContentValues);
        long pubId = insertDb.insert(PicappContract.PublicationEntry.TABLE_NAME, null, pContentValues);

        ContentValues contentValues = PicappContract.BookmarkEntry.parse(userId, pubId);

        long id = insertDb.insert(PicappContract.BookmarkEntry.TABLE_NAME, null, contentValues);

        assertNotEquals(-1, id);

        SQLiteDatabase readDb = helper.getReadableDatabase();

        Cursor cursor = readDb.query(PicappContract.BookmarkEntry.TABLE_NAME,
                null,
                PicappContract.BookmarkEntry._ID + "= ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        assertNotNull(cursor);

        cursor.moveToFirst();
        int pId = cursor.getInt(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_PUBLICATION_ID));
        int uId = cursor.getInt(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_USER_ID));

        assertEquals(pubId, pId);
        assertEquals(userId, uId);

        cursor.close();
        insertDb.close();
        readDb.close();
    }

    @Test
    public void testBookmarkExist(){

        PicItem picItem = getPicItem();
        UserItem userItem = getUserItem();

        assertNotNull(picItem);
        assertNotNull(userItem);

        PicSqlHelper helper = new PicSqlHelper(getContext());
        SQLiteDatabase insertDb = helper.getWritableDatabase();

        ContentValues uContentValues = PicappContract.UserEntry.parse(userItem);
        ContentValues pContentValues = PicappContract.PublicationEntry.parse(picItem);

        long userId = insertDb.insert(PicappContract.UserEntry.TABLE_NAME, null, uContentValues);
        long pubId = insertDb.insert(PicappContract.PublicationEntry.TABLE_NAME, null, pContentValues);

        ContentValues contentValues = PicappContract.BookmarkEntry.parse(userId, pubId);

        long id = insertDb.insert(PicappContract.BookmarkEntry.TABLE_NAME, null, contentValues);

        assertNotEquals(-1, id);

        SQLiteDatabase readDb = helper.getReadableDatabase();

        long userDbId = PicappContract.UserEntry.getUserDbId(readDb, userItem);
        long pubDbId = PicappContract.PublicationEntry.buildPublicationUniqueId(readDb, picItem);

        Cursor cursor = readDb.query(PicappContract.BookmarkEntry.TABLE_NAME,
                null,
                PicappContract.BookmarkEntry.COLUMN_USER_ID + "= ? AND "+
                        PicappContract.BookmarkEntry.COLUMN_PUBLICATION_ID+"= ?",
                new String[]{String.valueOf(userDbId), String.valueOf(pubDbId)},
                null, null, null);

        boolean exist = cursor != null && cursor.getCount() > 0;

        assertTrue(exist);

        cursor.close();
        insertDb.close();
        readDb.close();
    }

    @Test
    public void testDeleteBookmarkItem(){
        PicItem picItem = getPicItem();
        UserItem userItem = getUserItem();

        assertNotNull(picItem);
        assertNotNull(userItem);

        PicSqlHelper helper = new PicSqlHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues uContentValues = PicappContract.UserEntry.parse(userItem);
        ContentValues pContentValues = PicappContract.PublicationEntry.parse(picItem);

        long userId = db.insert(PicappContract.UserEntry.TABLE_NAME, null, uContentValues);
        long pubId = db.insert(PicappContract.PublicationEntry.TABLE_NAME, null, pContentValues);

        ContentValues contentValues = PicappContract.BookmarkEntry.parse(userId, pubId);

        long id = db.insert(PicappContract.BookmarkEntry.TABLE_NAME, null, contentValues);

        assertNotEquals(-1, id);

        int uCount = db.delete(PicappContract.UserEntry.TABLE_NAME,
                PicappContract.UserEntry._ID + "= ?",
                new String[]{String.valueOf(userId)});

        int bCount = db.delete(PicappContract.BookmarkEntry.TABLE_NAME,
                PicappContract.BookmarkEntry.COLUMN_USER_ID + "= ? AND " +
                        PicappContract.BookmarkEntry.COLUMN_PUBLICATION_ID + "= ?",
                new String[]{String.valueOf(userId), String.valueOf(pubId)});

        int pCount = db.delete(PicappContract.UserEntry.TABLE_NAME,
                PicappContract.PublicationEntry._ID + "= ?",
                new String[]{String.valueOf(pubId)});

        //Deletion will cascade so deleting the user will do the same with the others tables
        assertEquals(1, uCount);
        assertEquals(0, bCount);
        assertEquals(0, pCount);

        db.close();
    }

    @Test
    public void testGetAllBookmarks(){
        PicItem picItem = getPicItem();
        UserItem userItem = getUserItem();

        assertNotNull(picItem);
        assertNotNull(userItem);

        PicSqlHelper helper = new PicSqlHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteDatabase readDb = helper.getReadableDatabase();

        ContentValues uContentValues = PicappContract.UserEntry.parse(userItem);
        ContentValues pContentValues = PicappContract.PublicationEntry.parse(picItem);

        long userId = db.insert(PicappContract.UserEntry.TABLE_NAME, null, uContentValues);
        long pubId = db.insert(PicappContract.PublicationEntry.TABLE_NAME, null, pContentValues);

        ContentValues contentValues = PicappContract.BookmarkEntry.parse(userId, pubId);

        db.insert(PicappContract.BookmarkEntry.TABLE_NAME, null, contentValues);

        Cursor cursor = PicContentProvider.BOOKMARK_QUERY_BUILDER.query(
                readDb, PicappContract.PROJECTION_BOOKMARK_ALL, null, null, null, null, null);

        assertNotNull(cursor);
        assertTrue(cursor.getCount() > 0);

        cursor.close();
        db.close();
        readDb.close();
    }

    private Context getContext(){
        return InstrumentationRegistry.getTargetContext();
    }

    private PicItem getPicItem(){

        final String picResponse = "{ \"photos\": { \"page\": 1, \"pages\": \"1000\", \"perpage\": 1, \"total\": \"1000\", \n" +
                "    \"photo\": [\n" +
                "      { \"id\": \"30135992985\", \"owner\": \"147769095@N04\", \"secret\": \"1f0dcd9f79\"," +
                " \"count_faves\": \"559\", \"count_comments\": 6, \"server\": \"5590\", \"farm\": 6, \"title\": \"20150827_225851844_iOS.jpg\", \"ispublic\": 1," +
                " \"isfriend\": 0, \"isfamily\": 0, \"license\": 0, \n" +
                "        \"description\": { \"_content\": \"\" }, \"dateupload\": \"1475689285\", \"lastupdate\": \"1475689288\", " +
                "\"datetaken\": \"2015-08-27 18:58:51\", \"datetakengranularity\": 0, \"datetakenunknown\": 0, " +
                "\"ownername\": \"stanley882@ymail.com\", \"iconserver\": 0, \"iconfarm\": 0, \"views\": 0, \"tags\": \"\", \"machine_tags\": \"\", " +
                "\"originalsecret\": \"26922c3a98\", \"originalformat\": \"jpg\", \"latitude\": 0, \"longitude\": 0, \"accuracy\": 0, \"context\": 0," +
                " \"media\": \"photo\", \"media_status\": \"ready\", \"url_sq\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_s.jpg\", " +
                "\"height_sq\": 75, \"width_sq\": 75, \"url_t\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_t.jpg\"," +
                " \"height_t\": 100, \"width_t\": 75, \"url_s\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_m.jpg\", " +
                "\"height_s\": \"240\", \"width_s\": \"180\", \"url_q\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_q.jpg\", " +
                "\"height_q\": \"150\", \"width_q\": \"150\", \"url_m\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79.jpg\", " +
                "\"height_m\": \"500\", \"width_m\": \"375\", \"url_n\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_n.jpg\"," +
                " \"height_n\": \"320\", \"width_n\": \"240\", \"url_z\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_z.jpg\"," +
                " \"height_z\": \"640\", \"width_z\": \"480\", \"url_c\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_c.jpg\", " +
                "\"height_c\": \"800\", \"width_c\": \"600\", \"url_l\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_1f0dcd9f79_b.jpg\", " +
                "\"height_l\": \"1024\", \"width_l\": \"768\", \"url_o\": \"https:\\/\\/farm6.staticflickr.com\\/5590\\/30135992985_26922c3a98_o.jpg\", " +
                "\"height_o\": \"2448\", \"width_o\": \"3264\", \"pathalias\": \"\" }\n" +
                "    ] }, \"stat\": \"ok\" }";

        try {
            return new FlickrPhotosClient().parse(picResponse).get(0);
        } catch (JSONException e) {
            return null;
        }
    }

    private UserItem getUserItem(){
        final String userResponse = "{ \"person\": { \"id\": \"147769095@N04\", \"nsid\": \"147769095@N04\", \"ispro\": 0, \"can_buy_pro\": 0, \"iconserver\": 0, \"iconfarm\": 0, \"path_alias\": \"\", \"has_stats\": 0, \n" +
                "    \"username\": { \"_content\": \"stanley882@ymail.com\" }, \n" +
                "    \"realname\": { \"_content\": \"Stanley Woo\" }, \n" +
                "    \"location\": { \"_content\": \"\" }, \n" +
                "    \"description\": { \"_content\": \"\" }, \n" +
                "    \"photosurl\": { \"_content\": \"https:\\/\\/www.flickr.com\\/photos\\/147769095@N04\\/\" }, \n" +
                "    \"profileurl\": { \"_content\": \"https:\\/\\/www.flickr.com\\/people\\/147769095@N04\\/\" }, \n" +
                "    \"mobileurl\": { \"_content\": \"https:\\/\\/m.flickr.com\\/photostream.gne?id=147736956\" }, \n" +
                "    \"photos\": { \n" +
                "      \"firstdatetaken\": { \"_content\": \"2009-02-22 12:54:42\" }, \n" +
                "      \"firstdate\": { \"_content\": \"1475682524\" }, \n" +
                "      \"count\": { \"_content\": \"806\" } } }, \"stat\": \"ok\" }";

        try {
            return new FlickerUserClient().parse(userResponse);
        } catch (JSONException e) {
            return null;
        }
    }
}
