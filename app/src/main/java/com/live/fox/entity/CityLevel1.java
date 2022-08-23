/**
  * Copyright 2019 bejson.com 
  */
package com.live.fox.entity;


import java.util.List;



/**
 * Auto-generated: 2019-07-08 18:15:46
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CityLevel1 {

    private String name;
    private List<CityLevel2> city;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setCity(List<CityLevel2> city) {
         this.city = city;
     }
     public List<CityLevel2> getCity() {
         return city;
     }



}