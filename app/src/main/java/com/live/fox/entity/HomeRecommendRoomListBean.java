package com.live.fox.entity;

import java.util.List;

public class HomeRecommendRoomListBean {

    private List<ChannelListData> list;

    public List<ChannelListData> getList() {
        return list;
    }

    public void setList(List<ChannelListData> list) {
        this.list = list;
    }

    public static class ChannelListData
    {
        private int id;
        private String channelName;
        private int channelId;
        private String channelIcon;
        private int sort;
        private List<RoomListBean> roomAllList;
        private List<RoomListBean> roomCrList;
        private List<RoomListBean> roomGameList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public String getChannelIcon() {
            return channelIcon;
        }

        public void setChannelIcon(String channelIcon) {
            this.channelIcon = channelIcon;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public List<RoomListBean> getRoomAllList() {
            return roomAllList;
        }

        public void setRoomAllList(List<RoomListBean> roomAllList) {
            this.roomAllList = roomAllList;
        }

        public List<RoomListBean> getRoomCrList() {
            return roomCrList;
        }

        public void setRoomCrList(List<RoomListBean> roomCrList) {
            this.roomCrList = roomCrList;
        }

        public List<RoomListBean> getRoomGameList() {
            return roomGameList;
        }

        public void setRoomGameList(List<RoomListBean> roomGameList) {
            this.roomGameList = roomGameList;
        }
    }

}
