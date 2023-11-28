package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class HotelSearchTest {
    @Autowired
    private IHotelService hotelService;
    private RestHighLevelClient client;

    @Test
    void testMAatchAll() throws IOException {
        //1准备Request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
//3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4解析响应
        handleRespone(response);

    }

    @Test
    void testMAatch() throws IOException {
        //1准备Request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        request.source().query(QueryBuilders.matchQuery("all", "如家"));
//3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleRespone(response);
    }

    @Test
    void testBool() throws IOException {
        //1准备Request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        //2.1准备BooleanQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //2.2添加term
        boolQuery.must(QueryBuilders.termQuery("city", "上海"));
        //2.3添加ran'ge
        boolQuery.filter(QueryBuilders.rangeQuery("price").lte(250));
        request.source().query(boolQuery);
//3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleRespone(response);
    }

    @Test
    void testPageAndSort() throws IOException {
        //页码 ，每页大小
        int page=1,size=5;
        //1准备Request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
//2.1query
        request.source().query(QueryBuilders.matchAllQuery());
        //2.2排序sort
        request.source().sort("price", SortOrder.ASC);
        //2.3分页 from,size
        request.source().from((page-1)*size).size(size);
//3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleRespone(response);
    }
    @Test
    void testHighlight() throws IOException {

        //1准备Request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
//2.1query
request.source().query(QueryBuilders.matchQuery("all","如家"));
//2.2高亮
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
//3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleRespone(response);
    }

    private static void handleRespone(SearchResponse response) {
        //4解析响应
        SearchHits searchHits = response.getHits();
        //4.1获取总条数
        TotalHits total = searchHits.getTotalHits();
        System.out.println("共搜索到" + total + "条数据");
        //4.2文档数据
        SearchHit[] hits = searchHits.getHits();
        //4.3遍历
        for (SearchHit hit : hits) {
            //获取文档source
            String json = hit.getSourceAsString();
            //反序列化
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            //获取高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields)){
                //根据字段名获取高亮
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField!=null){
                    //获取高亮值
                    String name = highlightField.getFragments()[0].string();
                    //覆盖高亮结果
                    hotelDoc.setName(name);
                }
            }


            System.out.println("hotelDoc=" + hotelDoc);
        }
    }

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.37.131:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
}
