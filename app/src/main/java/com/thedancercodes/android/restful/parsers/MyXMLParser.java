package com.thedancercodes.android.restful.parsers;

/**
 * Created by TheDancerCodes on 07/02/2018.
 */

import com.thedancercodes.android.restful.model.DataItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MyXMLParser {

    public static DataItem[] parseFeed(String content) {

        try {

            boolean inItemTag = false;
            String currentTagName = "";
            DataItem currentItem = null;
            List<DataItem> itemList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            // Contains current event from the Parser
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("item")) {
                            inItemTag = true;
                            currentItem = new DataItem();
                            itemList.add(currentItem);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            inItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (inItemTag && currentItem != null) {
                            try {
                                switch (currentTagName) {
                                    case "itemName":
                                        currentItem.setItemName(text);
                                        break;
                                    case "description":
                                        currentItem.setDescription(text);
                                        break;
                                    case "category":
                                        currentItem.setCategory(text);
                                        break;
                                    case "price":
                                        currentItem.setPrice(Double.parseDouble(text));
                                        break;
                                    case "sort":
                                        currentItem.setSort(Integer.parseInt(text));
                                        break;
                                    case "image":
                                        currentItem.setImage(text);
                                    default:
                                        break;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }

                eventType = parser.next();

            } // end while loop

            // Turn list into an array and return data
            DataItem[] dataItems = new DataItem[itemList.size()];
            return itemList.toArray(dataItems);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
