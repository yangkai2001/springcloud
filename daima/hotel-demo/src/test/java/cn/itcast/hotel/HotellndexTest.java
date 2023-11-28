package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static cn.itcast.hotel.constants.HotelConstant.MAPPING_TEMPLATE;

@SpringBootTest
public class HotellndexTest {
    @Autowired
    private IHotelService hotelService;
    private RestHighLevelClient client;
//新增文档
@Test
void testAddDocument() throws IOException {
    //根据id查询酒店数据
    Hotel hotel=hotelService.getById(61083l);
    //转换为文档类型
    HotelDoc hotelDoc=new HotelDoc(hotel);
//1.准备Request对象
    IndexRequest request=new IndexRequest("hotel").id(hotel.getId().toString());
//2准备json文件
    request.source(JSON.toJSONString(hotelDoc),XContentType.JSON);

    //3发送请求
    client.index(request,RequestOptions.DEFAULT);
}
//查询文档数据
@Test
void testGetCoumentById() throws IOException {
    //1.准备request
GetRequest request= new GetRequest("hotel","61083");
    //2发送请求，得到响应
    GetResponse response = client.get(request, RequestOptions.DEFAULT);
    //3解析响应结果
    String json = response.getSourceAsString();
    HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
    System.out.println(hotelDoc);

}
    //更新文档数据
    @Test
    void testUpdaCoumentById() throws IOException {
        //1.准备request
        UpdateRequest request= new UpdateRequest("hotel","61083");
        //2准备请求参数
        request.doc("price","952",
        "starName","六钻");
        //2发送请求，得到响应
        client.update(request, RequestOptions.DEFAULT);

    }

    //删除文档数据
    @Test
    void testDeleDoument()throws IOException{
    //准备request
        DeleteRequest request=new DeleteRequest("hotel","61083");
        //发送请求
        client.delete(request,RequestOptions.DEFAULT);
    }

    //批处理创建酒店数据
    @Test
    void testBulkRequest()throws IOException{
//批量查询酒店数据
        List<Hotel> hotels=hotelService.list();
for (Hotel hotel:hotels){
    HotelDoc hotelDoc=new HotelDoc(hotel);
}
//1创建Request
        BulkRequest request=new BulkRequest();
//2.准备参数，添加多个新增的request
for (Hotel hotel:hotels){
//转换为文档类型的hotelDoc
    HotelDoc hotelDoc=new HotelDoc(hotel);
    //创建新文档的request对象
    request.add(new IndexRequest("hotel").id(hotelDoc.getId().toString())
            .source(JSON.toJSONString(hotelDoc),XContentType.JSON)
    );
    //3.发送数据
    client.bulk(request,RequestOptions.DEFAULT);

}




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
