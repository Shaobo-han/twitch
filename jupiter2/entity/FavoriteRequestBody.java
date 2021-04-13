package com.laioffer.jupiter2.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequestBody {
    //@JsonProperty("favorite")其实可以吧这个annotation放在这里。
    private final Item favoriteItem;

    @JsonCreator//这个annotation是把前端发送的request的body的json格式的String --> java object（request的对象）
    public FavoriteRequestBody(@JsonProperty("favorite") Item favoriteItem) {
                            //此处的@是把json的key == "favorite"对应的value跟 Item类的对象favoriteItem的内容对应起来(理解为：
                            //key是favoriteItem，value是favoriteItem{}里的内容)。此时就完成了json格式的request转换成了java的request对象
        this.favoriteItem = favoriteItem;
    }

    public Item getFavoriteItem() {
        return favoriteItem;
    }

}
