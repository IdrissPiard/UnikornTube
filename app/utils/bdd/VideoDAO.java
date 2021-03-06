package utils.bdd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Video;

public class VideoDAO {
	
	private final static String _tableName = "videos";
	//private final static String[] _fieldsName = { "title", "nb_like", "nb_dislike", "nd_view", "id_user, uploaded"};

	/**
	 * Ajoute une video
	 * @param title
	 * @param description
	 * @param idUser
	 * @return idVideo si ok et -1 si fail
	 * @throws SQLException
	 */
	public static int create(String title, String description, int idUser, String[] tags) {
		Date dt = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ResultSet locRs = MysqlConnection.executeUpdateGetResult("INSERT INTO "+_tableName+" ( title, description, nb_like, nb_dislike, nb_view, id_user, uploaded) VALUES ('"+ title + "', '"+description+"', 0, 0, 0, "+idUser+", '"+ sdf.format(dt) +"')");
			
			if(locRs.next()){
				int idVideo = locRs.getInt(1);
				if(tags != null)
					for(String tag: tags)
						addTagToVideo(idVideo, tag);
	    		return idVideo;
			}
			return -1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Supprime une video avec son id
	 * @param idVideo
	 * @throws SQLException
	 */
	public static void remove(int idVideo) {
		
		String locS = "DELETE FROM "+_tableName+" WHERE id = "+idVideo;
		
		try {
			MysqlConnection.executeUpdate(locS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public Video update(Video parObject) throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/**
	 * Recherche
	 * @param parId
	 * @return
	 * @throws SQLException
	 */
	public static Video find(int parId) {
		
		try {
		
			ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM "+_tableName+" WHERE id = "+parId);
			
			if(locRs.next()) {
	    		return (new Video(locRs.getInt(1), locRs.getString(2), locRs.getInt(3), locRs.getInt(4), locRs.getInt(5), locRs.getInt(6), locRs.getString(7), locRs.getString(8)));
	    	}
			
			return null;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	public List<Video> findAll() throws SQLException {
//		
//		ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM "+_tableName);
//		
//		List<Video> locAllVideos = new ArrayList<Video>();
////		while(locRs.next())
////			locAllVideos.add(new Video(locRs.getLong(1), locRs.getString(2), locRs.getString(3), locRs.getInt(4), locRs.getInt(5), locRs.getInt(6), locRs.getLong(7)));
//		return locAllVideos;
//	}
	
	/**
	 * Augmente le nombre de vue
	 * @param idVideo
	 * @return
	 * @throws SQLException
	 */
	public static int addView(int idVideo) {
		
		try {
			ResultSet locSearch = MysqlConnection.executeQuery("SELECT * FROM videos WHERE id = "+ idVideo );
			
			if(!locSearch.next()){
	    		return 1;
			}
			
			if(MysqlConnection.executeUpdate("UPDATE videos SET nb_view = "+(locSearch.getInt(5)+1)+" WHERE id = "+idVideo) > 0) {
				return 0;
			}
			
			return 2;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -42;
		}
		
	}
	
	
	
	/**
	 * Vote sur une video +1 ou -1
	 * @param idVideo
	 * @param idUser
	 * @param vote
	 * @return 0 si ok, 1 si user inexistant, 2 si video inexistante, 3 si déjà voté, 4 si fail
	 * @throws SQLException
	 */
	public static int vote(int idVideo, int idUser, int vote) {
		
		try {
			ResultSet locSearch = MysqlConnection.executeQuery("SELECT * FROM videos WHERE id = "+ idVideo );
		
			if(!locSearch.next()){
	    		return 1;
			}
			ResultSet locSearch2 = MysqlConnection.executeQuery("SELECT * FROM users WHERE id = "+ idUser );
			if(!locSearch2.next()){
	    		return 2;
			}
			ResultSet locSearch3 = MysqlConnection.executeQuery("SELECT * FROM likes WHERE id_user = "+ idUser + " AND id_video = "+idVideo);
			if(locSearch3.next()){
	    		return 3;
			}
			
			Date dt = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(MysqlConnection.executeUpdate("INSERT INTO likes (viewedtime, value, id_user, id_video) VALUES ('"+sdf.format(dt)+"', "+(vote+1)+", "+idUser+", "+idVideo+")") > 0) {
				
				if(vote == 1 && MysqlConnection.executeUpdate("UPDATE videos SET nb_like = "+(locSearch.getInt(3)+1)+" WHERE id = "+idVideo) > 0){
					return 0;
				} else {
					if(MysqlConnection.executeUpdate("UPDATE videos SET nb_dislike = "+(locSearch.getInt(4)+1)+" WHERE id = "+idVideo) > 0) {
						return 0;
					}
				}
				
			}
			
			return 4;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -42;
		}
		
	}
	
	/**
	 * Cherche les 15 dernières vidéos
	 * @return
	 */
	public static List<Video> getLastVideo() {
		
		try {
			ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM "+_tableName+" ORDER BY uploaded DESC LIMIT 15");
		
		
			List <Video> videos = new ArrayList<Video>();
			
			while(locRs.next()) {
	    		videos.add(new Video(locRs.getInt(1), locRs.getString(2), locRs.getInt(3), locRs.getInt(4), locRs.getInt(5), locRs.getInt(6), locRs.getString(7), locRs.getString(8)));
	    	}
			
			return videos;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Cherche les 15 vidéos les plus vues
	 * @return
	 */
	public static List<Video> getPopularVideo() {
		
		try {
			ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM "+_tableName+" ORDER BY nb_view DESC LIMIT 15");
		
		
			List <Video> videos = new ArrayList<Video>();
			
			while(locRs.next()) {
	    		videos.add(new Video(locRs.getInt(1), locRs.getString(2), locRs.getInt(3), locRs.getInt(4), locRs.getInt(5), locRs.getInt(6), locRs.getString(7), locRs.getString(8)));
	    	}
			
			return videos;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Recherche les videos correspondant
	 * @param search
	 * @return
	 */
	public static List<Video> recherche(String search) {
		
		try {
			ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM "+_tableName+" WHERE title LIKE '%"+search+"%' LIMIT 15");
		
		
			List <Video> videos = new ArrayList<Video>();
			
			while(locRs.next()) {
	    		videos.add(new Video(locRs.getInt(1), locRs.getString(2), locRs.getInt(3), locRs.getInt(4), locRs.getInt(5), locRs.getInt(6), locRs.getString(7), locRs.getString(8)));
	    	}
			
			return videos;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Ajoute un tag à une vidéo. Si le tag n'existe pas cela le crée
	 * @param idVideo
	 * @param tag
	 * @return 0 si ok, 1 si fail
	 */
	public static int addTagToVideo(int idVideo, String tag) {
		try {
			ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM tags WHERE tag = '"+tag+"'");
			if(locRs.next()){
				if(MysqlConnection.executeUpdate("INSERT INTO tag_video (id_video, id_tag) VALUES ("+idVideo+", "+locRs.getInt(1)+")") > 0){
					return 0;
				} 
				return 1;
			} else {
				ResultSet locRs2 = MysqlConnection.executeUpdateGetResult("INSERT INTO tags (tag) VALUES ('"+tag+"')");
				if(locRs2.next()) {
					if(MysqlConnection.executeUpdate("INSERT INTO tag_video (id_video, id_tag) VALUES ("+idVideo+", "+locRs2.getInt(1)+")") > 0) {
						return 0;
					}
				}
				return 1;
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -42;
		}
	}
	
	/**
	 * Cherche tous les tags d'une video
	 * @param idVideo
	 * @return liste de string (tag)
	 */
	public static List<String> findTagVideo(int idVideo) {
		
		try {
			ResultSet locRs = MysqlConnection.executeQuery("SELECT * FROM tag_video WHERE id_video = "+idVideo);
			List<String> tags = new ArrayList<String>();
			while(locRs.next()){
				ResultSet locRs2 = MysqlConnection.executeQuery("SELECT * FROM tags WHERE id = "+locRs.getInt(2));
				if(locRs2.next())
					tags.add(locRs2.getString(2));
			}
			return tags;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	

}
