RESTful API 设计
---
###培养计划
 1. GET /plan 返回培养计划
    response:

        {
          "plans": [
            {
              "majorID": "专业号",
              "majorName":"专业名",
              "courses":[
                {
                  "id":"课程号",
                  "name":"课程名",
                  "credit":"学分",
                  "learningLength":"课时",
                  "recommend":"建议修课时间",
                  "type":"类型",
                  "isRequired":"是否必修",
                  "experiment":"实验时间"
                },
                {}
              ],
              "common":"通识学分"
            },
            {}
          ]
        }
     
 2. GET /plan/{majorID} 返回指定专业的培养计划
    response: 
       
        {
            "plan": {
              "majorID": "专业号",
              "majorName":"专业名",
              "courses":[
                {
                  "id":"课程号",
                  "name":"课程名",
                  "credit":"学分",
                  "learningLength":"课时",
                  "recommend":"建议修课时间",
                  "type":"类型",
                  "isRequired":"是否必修",
                  "experiment":"实验时间"
                },
                {}
              ],
              "common":"通识学分"
            }
        }
        
###待选课程

 1. GET /course 返回所有课程
    response:

        {
          "courses":[
            {
              "id":"课程号",
              "no":"课序号",
              "name":"课程名",
              "credit":"学分",
              "teacher":"老师",
              "timeAndLength":[
                {
                  "time":"时间",
                  "length":"长度"
                },
                {}
                ],
              "class":"建议选课班级",
              "selective":"是否可选",
              "type":"课程性质（必修，限选，通识课程）",
              "amount":"课余量",
              "remark":"选课限制备注"
            },
            {}
            ]
        }

 2. GET /course/{courseID} 
    response:

        {
          "course":{
            "id":"课程号",
            "no":"课序号",
            "name":"课程名",
            "credit":"学分",
            "teacher":"老师",
            "timeAndLength":[
              {
                "time":"时间",
                "length":"长度"
              },
              {}
            ],
            "class":"建议选课班级",
            "selective":"是否可选",
            "type":"课程性质（必修，限选，通识课程）",
            "amount":"课余量",
            "remark":"选课限制备注"
          }
        }
        
###用户信息
 1. GET /user 返回所有用户
    response:

        {
          "users":[
            {
              "id":"用户号",
              "type":"用户类型",
              "major":"",
              "courseTable":[
                {
                  "id":"课程号",
                  "no":"课序号",
                  "name":"课程名",
                  "credit":"学分",
                  "teacher":"老师",
                  "address":"上课地点",
                  "timeAndLength":[
                    {
                      "time":"时间",
                      "length":"长度"
                    },
                    {}
                  ],
                  "type":"课程性质（必修，限选，通识课程）"
                },
                {}
              ],
              "credit":"剩余学分"
            },
            {}
          ]
        }

 2. GET /usr/{id} 返回指定用户
    response:

        {
          "user":{
              "id":"用户号",
              "type":"用户类型",
              "major":"",
              "courseTable":[
                {
                  "id":"课程号",
                  "no":"课序号",
                  "name":"课程名",
                  "credit":"学分",
                  "teacher":"老师",
                  "address":"上课地点",
                  "timeAndLength":[
                    {
                      "time":"时间",
                      "length":"长度"
                    },
                    {}
                  ],
                  "type":"课程性质（必修，限选，通识课程）",
                  "averageScore":"平时成绩",
	              "finalScore":"期末成绩",
	              "totalScore":"总成绩"
                },
                {}
              ],
              "credit":"剩余学分"
          }
        }

 3. POST /user/{id}/course 
    body： 

        {
            "id":"课程号"
        }

    response:
    
        {
           "courseTable":[
              {
                "id":"课程号",
                "no":"课序号",
                "name":"课程名",
                "credit":"学分",
                "teacher":"老师",
                "address":"上课地点",
                "timeAndLength":[
                  {
                    "time":"时间",
                    "length":"长度"
                  },
                  {}
                ],
                "type":"课程性质（必修，限选，通识课程）"
                "averageScore":"平时成绩",
	            "finalScore":"期末成绩",
	            "totalScore":"总成绩"
              },
              {}
            ],
            "credit":"剩余学分"
        }
        
 4. DELETE /user/{id}/course
    request:

        {
            "id":"课程号"
        }
    
    response:
    
        {
            
        }
        
        
###错误

 1. 课程满了
 2. 学分不够了
 3. 时间冲突
 4. 不符合重修条件
 5. 找不到这个人/课/专业