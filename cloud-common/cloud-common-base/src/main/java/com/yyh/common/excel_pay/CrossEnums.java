package com.yyh.common.excel_pay;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/31
 * \* Time: 10:09
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public enum CrossEnums {

    a(1,"T1",18,"5号场1进1出",80L,"5号场出口",5),
    b(1,"T1",19,"6号场1进1出",82L,"六号场出口",6),
    c(1,"T1",20,"楼前2进3出",86L,"楼前出口1",1),
    d(1,"T1",20,"楼前2进3出",89L,"楼前出口2",2),
    e(1,"T1",20,"楼前2进3出",90L,"楼前出口3",3),
    f(1,"T1",20,"楼前2进3出",389838466164346882L,"楼前出口4",4),
    g(2,"T2",21,"二层3进2出",91L,"出口13",13),
    h(2,"T2",21,"二层3进2出",92L,"出口14",14),
    i(2,"T2",22,"一层8进8出临停区",101L,"出口1",1),
    j(2,"T2",22,"一层8进8出临停区",102L,"出口2",2),
    k(2,"T2",22,"一层8进8出临停区",103L,"出口3",3),
    l(2,"T2",22,"一层8进8出临停区",104L,"出口4",4),
    m(2,"T2",22,"一层8进8出临停区",105L,"出口5",5),
    n(2,"T2",28,"一层8进8出长期区",147L,"出口8",8),
    o(2,"T2",28,"一层8进8出长期区",148L,"出口9",9),
    p(2,"T2",28,"一层8进8出长期区",149L,"出口10",10),
    q(2,"T2",28,"一层8进8出长期区",151L,"B2内环出口15",15),
    r(2,"T2",28,"一层8进8出长期区",153L,"B2外环出口",16),
    s(3,"T3",17,"P7",78L,"P7出口",19),
    t(3,"T3",23,"近端P8员工1进2出",111L,"近端员工P8出口1",18),
    u(3,"T3",23,"近端P8员工1进2出",112L,"近端员工P8出口2",19),
    v(3,"T3",25,"GTC10进10出",123L,"GTC出口1",1),
    w(3,"T3",25,"GTC10进10出",125L,"GTC出口3",3),
    x(3,"T3",25,"GTC10进10出",126L,"GTC出口4",4),
    y(3,"T3",25,"GTC10进10出",127L,"GTC出口5",5),
    z(3,"T3",25,"GTC10进10出",128L,"GTC出口6",6),
    aa(3,"T3",25,"GTC10进10出",129L,"GTC出口7",7),
    bb(3,"T3",25,"GTC10进10出",130L,"GTC出口8",8),
    cc(3,"T3",25,"GTC10进10出",131L,"GTC出口9",9),
    dd(3,"T3",25,"GTC10进10出",132L,"GTC出口10",10),
    ee(3,"T3",25,"GTC10进10出",133L,"GTC出口11",11),
    ff(3,"T3",25,"GTC10进10出",134L,"GTC出口12",12),
    gg(3,"T3",25,"GTC10进10出",135L,"GTC出口14",14),
    qq(3,"T3",25,"GTC10进10出",136L,"GTC出口15",15),
    zz(3,"T3",25,"GTC10进10出",138L,"GTC入口14",14),
    ;

    private Integer terminalId;

    private String terminalName;

    private Integer parkingareaId;

    private String parkingareaName;

    private Long crossId;

    private String crossName;

    private Integer lane;

    CrossEnums (Integer terminalId, String terminalName, Integer parkingareaId, String parkingareaName, Long crossId, String crossName, Integer lane) {
        this.terminalId = terminalId;
        this.terminalName = terminalName;
        this.parkingareaId = parkingareaId;
        this.parkingareaName = parkingareaName;
        this.crossId = crossId;
        this.crossName = crossName;
        this.lane = lane;
    }


    public  static  CrossEnums getCrossInfoByLane(Integer lane, Integer terminalId) {
        for (CrossEnums crossEnum: values()) {
            if (crossEnum.lane.equals(lane) && crossEnum.terminalId.equals(terminalId)) {
                return crossEnum;
            }
        }
        return null;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public Integer getParkingareaId() {
        return parkingareaId;
    }

    public void setParkingareaId(Integer parkingareaId) {
        this.parkingareaId = parkingareaId;
    }

    public String getParkingareaName() {
        return parkingareaName;
    }

    public void setParkingareaName(String parkingareaName) {
        this.parkingareaName = parkingareaName;
    }

    public Long getCrossId() {
        return crossId;
    }

    public void setCrossId(Long crossId) {
        this.crossId = crossId;
    }

    public String getCrossName() {
        return crossName;
    }

    public void setCrossName(String crossName) {
        this.crossName = crossName;
    }

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }
}