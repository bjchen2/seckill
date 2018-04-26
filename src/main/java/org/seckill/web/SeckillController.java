package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//这里不能用@RestController，因为下面有几个方法不是返回Json而是跳转到jsp界面
@Controller//类似于@Component和@Service，目的是将该类放进SpringIOC容器中
@RequestMapping("seckill")//代表url的模块，url:/模块/资源/{id}/细分
public class SeckillController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";//这里相当于会把所有数据渲染到/WEB-INF/jsp/list.jsp页面中,通过网址http://localhost:8080/seckill/list访问
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.geSeckillById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/lsit";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax接口调用的json，返回一个json，produces：返回浏览器的信息（默认为json，但这是一个好的习惯）
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    //使其返回一个json，该json的数据为返回对象的toString
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> seckillResult;
        try {
            //成功，响应数据dto->Exposer，该数据包含是否开启秒杀及秒杀开启和结束时间，秒杀地址等
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult<Exposer>(true, exposer);

        } catch (Exception e) {
            //失败，传入失败原因
            logger.error(e.getMessage(), e);
            seckillResult = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return seckillResult;
    }

    //执行秒杀
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    //@CookieValue:从cookie中读入value为seckillPhone的cookie值，required = false：当找不到时不报错
    public SeckillResult<SeckillExecution> execut(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                  @CookieValue(value = "userPhone", required = false) String userPhone) {
        //若检验复杂时可用SpringMVC valid
        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            System.out.println(seckillExecution);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e) {
            return new SeckillResult<SeckillExecution>(true, new SeckillExecution(SeckillStateEnum.REPEAT_KILL, seckillId));
        } catch (SeckillCloseException e) {
            return new SeckillResult<SeckillExecution>(true, new SeckillExecution(SeckillStateEnum.END, seckillId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillResult<SeckillExecution>(true,new SeckillExecution(SeckillStateEnum.INNER_ERROR,seckillId));
        }
    }

    //获取系统的当前时间
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        return new SeckillResult<Long>(true,new Date().getTime());
    }
}
