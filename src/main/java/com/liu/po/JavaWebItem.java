package com.liu.po;

/**
 * Created by xxg on 15-8-26.
 */
public class JavaWebItem {

    private String uuid;
    private String name;
    private int type;//git方式还是svn方式
    private String url;
    private String context_path;
    private int port;
    private String profile;
    private String branch;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

    public String getUuid() {
        return uuid;
    }

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContextPath() {
        return context_path;
    }

    public void setContextPath(String context_path) {
        this.context_path = context_path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
