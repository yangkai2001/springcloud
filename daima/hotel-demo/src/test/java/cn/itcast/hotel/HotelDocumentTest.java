package cn.itcast.hotel;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cn.itcast.hotel.constants.HotelConstant.MAPPING_TEMPLATE;


public class HotelDocumentTest {
    private RestHighLevelClient client;
    @Test
    void testInit(){
        System.out.println(client);
    }
    @Test
    void creatHotelIndex() throws IOException {
        //1.创建request对象
        CreateIndexRequest requerst=new CreateIndexRequest("hotel");
        //2准备请求的参数：Dsl语句
        requerst.source(MAPPING_TEMPLATE, XContentType.JSON);

        //3发送数据
        client.indices().create(requerst, RequestOptions.DEFAULT);
    }
//删除指定索引库
    @Test
    void testDeleteHotelIndex() throws IOException {
        //1.创建request对象
        DeleteIndexRequest requerst= new DeleteIndexRequest("hotel");
//        //2准备请求的参数：Dsl语句
//        requerst.source(MAPPING_TEMPLATE, XContentType.JSON);

        //3发送请求
        client.indices().delete(requerst, RequestOptions.DEFAULT);
    }
//查看指定索引库是否存在
    @Test
    void testExistHotelIndex() throws IOException {
        //1.创建request对象
        GetIndexRequest requerst= new GetIndexRequest("hotel");
//        //2准备请求的参数：Dsl语句
//        requerst.source(MAPPING_TEMPLATE, XContentType.JSON);

        //2.发送请求
        boolean exists = client.indices().exists(requerst, RequestOptions.DEFAULT);
        //3.输出
        System.out.println(exists?"索引库已存在！":"索引库不存在！");
    }



    @BeforeEach
    void setUp(){
        this.client=new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.37.131:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException{
        this.client.close();
    }
}
