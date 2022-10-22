package com.live.fox.entity;

import java.util.List;

public class HomeFragmentRoomListBean {

        private List<ChannelList> list;

        public List<ChannelList> getList() {
            return list;
        }

        public void setList(List<ChannelList> list) {
            this.list = list;
        }

        public static class ChannelList {
            private int id;
            private String channelName;
            private int channelId;
            private String channelIcon;
            private int sort;
            private List<RoomListBean> roomList;

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

            public List<RoomListBean> getRoomList() {
                return roomList;
            }

            public void setRoomList(List<RoomListBean> roomList) {
                this.roomList = roomList;
            }

        }
}
