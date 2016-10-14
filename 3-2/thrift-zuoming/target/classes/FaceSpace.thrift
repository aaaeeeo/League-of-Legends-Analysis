namespace java edu.uchicago.mpcs53013.FaceSpace

union UserID {
  1: i64 user_id;
}

union PostID {
  1: i64 post_id;
}

struct Location {
  1: optional string city;
  2: optional string state;
  3: optional string country;
}
struct Date {
  1: required i16 year;
  2: required i16 month;
  3: required i16 day;
}

enum GenderType {
  MALE = 1,
  FEMALE = 2,
  OTHER = 3
}
union UserPropertyValue {
  1: string full_name;
  2: GenderType gender;
  3: Location location;
  4: Date birthday;
}
struct UserProperty {
  1: required UserID user_id;
  2: required UserPropertyValue property;
}

enum FriendshipType {
  FRIEND = 1,
  UNFRIEND = 2,
  REQUESTING = 3
}
struct FriendshipEdge {
  1: required UserID user_id_1;
  2: required UserID user_id_2;
  3: required FriendshipType type;
}

struct PostContent {
	1:required PostID post_id;
	2:required string text;
	3:optional string img_url;
}

struct UserPostEdge {
	1:required UserID user_id;
	2:required PostID post_id;
	3:required bool visable;
}

union DataUnit {
  1: UserProperty user_property;
  2: PostContent post_content;
  3: FriendshipEdge friendship;
  4: UserPostEdge user_post;
}
struct TimeStamp {
  1: required i64 timestamp;
}
struct Data {
  1: required TimeStamp timestamp;
  2: required DataUnit dataunit;
}