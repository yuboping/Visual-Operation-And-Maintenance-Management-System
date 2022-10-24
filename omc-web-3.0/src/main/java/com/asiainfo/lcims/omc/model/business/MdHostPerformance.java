package com.asiainfo.lcims.omc.model.business;

import java.util.ArrayList;
import java.util.List;

public class MdHostPerformance {
	
	private String hostid;
	private String addr;
	private String hostname;
	private String hosttypename;
	private String nodename;
	//cpu 指标
	private String cpu_value;
	private String cpu_metricid;
	private MdMetricValue cpu;
	//内存
	private String memory_value;
	private String memory_metricid;
	private MdMetricValue memory;
	// 连通性
	private String connectable_value;
	private String connectable_metricid;
	private MdMetricValue connectable;
	// 进程
	private MdMetricValue process;
	private String process_metricid;
	private List<MdMetricValue> processlist = new ArrayList<MdMetricValue>();
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getNodename() {
		return nodename;
	}
	
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	
	public MdMetricValue getCpu() {
		return cpu;
	}
	
	public void setCpu(MdMetricValue cpu) {
		this.cpu = cpu;
	}
	
	public MdMetricValue getMemory() {
		return memory;
	}
	public void setMemory(MdMetricValue memory) {
		this.memory = memory;
	}
	public MdMetricValue getConnectable() {
		return connectable;
	}
	public void setConnectable(MdMetricValue connectable) {
		this.connectable = connectable;
	}
	public List<MdMetricValue> getProcesslist() {
		return processlist;
	}
	public void setProcesslist(List<MdMetricValue> processlist) {
		this.processlist = processlist;
	}
	
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCpu_value() {
		return cpu_value;
	}

	public void setCpu_value(String cpu_value) {
		this.cpu_value = cpu_value;
	}

	public String getCpu_metricid() {
		return cpu_metricid;
	}

	public void setCpu_metricid(String cpu_metricid) {
		this.cpu_metricid = cpu_metricid;
	}

	public String getMemory_value() {
		return memory_value;
	}

	public void setMemory_value(String memory_value) {
		this.memory_value = memory_value;
	}

	public String getMemory_metricid() {
		return memory_metricid;
	}

	public void setMemory_metricid(String memory_metricid) {
		this.memory_metricid = memory_metricid;
	}

	public String getConnectable_value() {
		return connectable_value;
	}

	public void setConnectable_value(String connectable_value) {
		this.connectable_value = connectable_value;
	}

	public String getConnectable_metricid() {
		return connectable_metricid;
	}

	public void setConnectable_metricid(String connectable_metricid) {
		this.connectable_metricid = connectable_metricid;
	}

    public MdMetricValue getProcess() {
        return process;
    }

    public void setProcess(MdMetricValue process) {
        this.process = process;
    }

    public String getHosttypename() {
        return hosttypename;
    }

    public void setHosttypename(String hosttypename) {
        this.hosttypename = hosttypename;
    }

    public String getProcess_metricid() {
        return process_metricid;
    }

    public void setProcess_metricid(String process_metricid) {
        this.process_metricid = process_metricid;
    }
	
}
