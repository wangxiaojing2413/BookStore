
package com.redis.learn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.redis.learn.util.RedisUtil;

public class RedisUtilTest {
	
	private static final Charset UTF_8 = Charset.forName("utf-8");

	@Test
	public void testString() {
		System.err.println("����String���Ϳ�ʼ>>\r\n\t");
		
		String key = "Program Name";
		String value = "Redis For Windows";
		String value1 = "Input Redis For bytes";
		RedisUtil.setString(key, value);
		RedisUtil.setBytes(key.getBytes(UTF_8), value1.getBytes(UTF_8));
		
		System.out.println("��Redis�л�ȡname:>>>\r\n\t");
		String val = RedisUtil.getString(key);
		System.out.println("���:\r\n\t" + val);
		
		System.out.println("��Redis�л�ȡname bytes:>>>>\r\n\t");
		byte[] bytes = RedisUtil.getBytes(key.getBytes(UTF_8));
		System.out.println("���bytes:\r\n\t" + Arrays.toString(bytes));
		val = new String(bytes, UTF_8);
		System.out.println("ת����String:\r\n\t" + val);
		
		System.out.println("ɾ��name�ļ�:\r\n\t");
		RedisUtil.delString(key);
		val = RedisUtil.getString(key);
		System.out.println("�ٴλ�ȡ:" + (val==null?"�ü��ѱ�ɾ��..":val));
	}
	
	@Test
	public void testMap() {
		
		System.err.println("����Redis For Map ��ʼ:>>>>");
		
		//�򵥵�string map
		Map<String, String> strMap = new HashMap<String, String>();
		//���ӵ��map
		Map<byte[], byte[]> bytesMap = new HashMap<byte[], byte[]>();
		
		//���Դ����µĵ�ַ
		strMap.put("OS", "Windows 10");
		strMap.put("Language", "ch");
		strMap.put("Tool", "Redis For Windows");
		String skey = "String For Redis";
		RedisUtil.addMap(skey, strMap);
		
		//�ӻ�ȡ���е�ֵ
		List<String> sList = RedisUtil.getMapVal(skey);
		System.out.println("���н��ֵ:" + sList);
		
		//���ո�����field˳�����ֵ
		sList = RedisUtil.getMapVal(skey, "Tool", "OS", "Language", "dd");
		//����ȡ����ֵ�������field��˳��һ��
		System.out.println("���ֵ[Tool, OS, Language, dd]:\r\n\t"+ sList);
		
		//������Redis�д洢����
		Person person = new Person("Johnny", 23, "��");
		//���л�����
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		ObjectInputStream bis = null;
		try {
			oos = new ObjectOutputStream(baos);
			//��������
			oos.writeObject(person);
			//��ȡ���л�֮����ֽ���
			byte[] bytes = baos.toByteArray();
			bytesMap.put(person.getName().getBytes(UTF_8), bytes);
			RedisUtil.addMap(person.getName().getBytes(UTF_8), bytesMap);
			
			//��Redis�ж�ȡ����
			List<byte[]> list= RedisUtil.getMapVal(person.getName().getBytes(UTF_8), person.getName().getBytes(UTF_8));
			if(list.size() == 1) {
				bytes = list.get(0);
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				bis = new ObjectInputStream(bais);
				Person p = (Person) bis.readObject();
				System.out.println("��ȡ������:" + p);
				
				bais.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if(baos != null) {
					baos.close();
				}
				if(null != oos) {
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//���Ѿ����ڵ�key��������ֵ��
		RedisUtil.addMapVal(person.getName().getBytes(UTF_8), "AddTest".getBytes(UTF_8), "Test Redis Adding A Val For Exist Key".getBytes(UTF_8));
		//��ȡ�ղ����ֵ
		System.out.println("��ȡ�ղ����ֵ:\r\n\t" + 
		new String(RedisUtil.getMapVal(person.getName().getBytes(UTF_8), "AddTest".getBytes(UTF_8)).get(0)));
		
		//�����򲻴��ڵ�Key�в�ֵ
		RedisUtil.addMapVal("AddNewKey", "AddNewMapKey", "AddNewMapVal");
		//�ܹ���ȡ��ֵ�����Ҳ˵���ڽ��в�����key�Ĳ�ֵʱ�����Զ���������ļ�ֵ���Ա��档
		System.out.println("���Ի�ȡ�ղ����ֵ:\r\n\t" + RedisUtil.getMapVal("AddNewKey", "AddNewMapKey"));
	}
	
	@Test
	public void testSet() {
		System.err.println("����Redis For Set ��ʼ:>>>>>>>");
		//��Redis���Ԫ��
		RedisUtil.addSet("AddNewSet", "set1", "set2", "set3");
		//��ȡset�е�ֵ
		System.out.println("Set���ϵĳ���:\r\n\t" + RedisUtil.getSetLength("AddNewSet"));
		System.out.println("Set����Ԫ��:\r\n\t" + RedisUtil.getSetVals("AddNewSet"));
		//�����Ƴ�Ԫ��
		RedisUtil.delSetVal("AddNewSet", "set2");
		System.out.println("Set���ϵĳ���:\r\n\t" + RedisUtil.getSetLength("AddNewSet"));
		System.out.println("Set����Ԫ��:\r\n\t" + RedisUtil.getSetVals("AddNewSet"));
		
		//�ж��Ƿ����Ԫ��
		System.out.println("�Ƿ����set2��ֵ:" + RedisUtil.isSetContain("AddNewSet", "set2"));
		System.out.println("�Ƿ����set2��ֵ:" + RedisUtil.isSetContain("AddNewSet", "set3"));
	}
	
	@Test
	public void testList() {
		System.err.println("����Redis For List ��ʼ:>>>>>>");
		//��List�����Ԫ��
		RedisUtil.addList("ValList", "List1", "List2", "List3");
		//��ȡList�е�ֵ
		System.out.println("Redis For List�е�ֵΪ:" + RedisUtil.getListAll("ValList"));
		//����list�ĵ�һ��Ԫ��
		System.out.println("������һ��Ԫ��:" + RedisUtil.popList("ValList"));
		System.out.println("Redis For List�е�ֵΪ:" + RedisUtil.getListAll("ValList"));
	}
}

class Person implements Serializable {
	private static final long serialVersionUID = 8737363017319228700L;
	private String name;
	private int age;
	private String sex;
	public Person(String name, int age, String sex) {
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}
	
}

