package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
23-Dec-23
gh /crazym8nd
*/
public class PostHandler extends BeanListHandler<Post> {
    private Connection connection;
    public PostHandler(Connection connection){
        super(Post.class);
        this.connection = connection;
    }

    @Override

    public List<Post> handle(ResultSet rs) throws SQLException{
        List<Post> posts = super.handle(rs);

        QueryRunner runner = new QueryRunner();
        BeanListHandler<Label> labelBeanListHandler = new BeanListHandler<>(Label.class);
        String query = "SELECT * FROM post_labels pl " +
                "LEFT JOIN labels l on pl.label_id = l.id " +
                "WHERE pl.post_id = ?";
        for (Post post : posts) {
            List<Label> labels = runner.query(connection, query, labelBeanListHandler, post.getId());
            post.setPostLabels(labels);
        }
        return posts;
    }
}
