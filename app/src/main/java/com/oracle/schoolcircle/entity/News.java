package com.oracle.schoolcircle.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class News implements Serializable{
    /**
     * news_id : 127
     * news_title : 校领导勉励新任辅导员担好光荣幸福的使命
     * news_content : 1月17日晚,校党委副书记、校长赵春明，党委副书记齐利平在教学管理楼三层会议室与近两年入职的辅导员进行了亲切座谈。赵春明勉励新任辅导员，政治辅导员是专为思想政治工作而组织的工作队伍，使命光荣，责任重大，工作很难，但要努力追求做好，会收获无数的发自内心的师生真情，是很幸福的。 座谈会上，与会辅导员一一做了简要自我介绍，汇报了从事辅导员工作以来体会和工作感悟，并结合自身实际，就工作中遇到的问题和对学校教学、管理等方面提出了意见建议。 赵春明听取了大家的汇报，首先代表校党委、校行政对近两年来新入职的辅导员送上了亲切的问候，感谢同志们为农大辅导员队伍注入了新鲜血液。他结合学校的实际，逐一解答了辅导员们提出的困难和问题，并提出三点期望与大家共勉。 一是要努力追求做好辅导员的三重境界。第一是安全健康，第二是做好学生日常管理、服务，第三是把学生培养成为高尚的人。安全健康是基础，是做好一切事情的起点；把日常工作做细了做好了，就能避免许多安全问题出现。学生日常管理服务工作是我们的基本职责和工作任务，做好了，就尽了基本的责任。把学生培养成为高尚的人，这是世界上最难的一件事。我们引导学生多读书、读经典，汲取人类文明智慧的精华。奉献可以使人高尚。我们要做在平时，通过志愿服务等培养学生为家人、为别人、为社会的奉献意识、奉献精神。 二是要传承山西农大人的优秀精神品质。这也是习近平总书记在全国思想政治工作会议上讲的“高校要培养什么样的人、如何培养人以及为谁培养人这个根本问题”。合格的山西农大学生要有高尚品德、自信气质、务实精神、专业本领，要传承农大人可信赖、务实、能吃苦的好品行。同志们刚参加工作，要在今后学习、工作、生活中更加用心地去体会、去感悟、去认同农大的历史发展、文化传承，影响教育引导我们的学生传承农大的优秀精神品质。 三是要做好思想政治工作。他勉励新任辅导员，政治辅导员是专为思想政治工作而组织的工作队伍，使命光荣，责任重大，工作很难，但努力追求做好，收获无数的发自内心的师生情，是很幸福的。他强调，做好辅导员工作要“师信、师德、师表、师能”四者齐聚一身，这是我们每一位从事教育工作者追求的目标。当老师要“信走天下”、“身正为师，德高为范”，“德高望重”，只有自己先信、先做、先行、做表率、做榜样，才能带领好自己的学生，才能真正成为学生口中的良师益友。 党委副书记齐利平指出，今天赵校长与新任一线辅导员进行了长达4个多小时的座谈，了解大家的学习、工作、生活的实际，这充分表明了学校领导对我们辅导员队伍的关心和重视，希望大家能把这份关心、温暖传递给全体辅导员，认真学习领会赵校长寄语大家的四点要求的同时，将要求内化于心，外化于行，转化为努力工作的目标和动力。今天，赵校长对大家提出的问题，一一认真、细致地解答，体现了他对大家的尊重。他提出的“师信”、“师德”、“师表”、“师能”是一个完整体系，希望我们的辅导员要深刻认识并系统学习领会这四个词之间的内在联系，在做好本职工作的同时，牢固树立“用发展来解决发展中的问题”的理念，注重学习能力的培养，努力提升思想认识水平，强化业务统筹能力的锻炼，以更加饱满的热情和积极的态度投入到学生工作中，为学校改革发展贡献力量。 党委宣传部、学工部的负责人参加座谈会。（学生工作部）
     * news_pic : upload/20170209/35ab4a8dae546a4f64a9b03ad9444ecf4d5a.jpg
     * news_date : 2017-02-09 11:02:49
     */

    private int news_id;
    private String news_title;
    private String news_content;
    private String news_pic;
    private String news_date;

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public String getNews_pic() {
        return news_pic;
    }

    public void setNews_pic(String news_pic) {
        this.news_pic = news_pic;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

}
